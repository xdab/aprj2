package pl.so5dz.aprj2.protocol.kiss;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.packet.Packet;
import pl.so5dz.aprj2.aprs.representation.impl.Ax25Representation;
import pl.so5dz.aprj2.protocol.PacketInputStream;

@Slf4j
public class KissInputStream extends PacketInputStream {
    private static final Ax25Representation kissPacketRepresentation = Ax25Representation.getInstance();

    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private boolean reading = false;
    private boolean escaped = false;

    public KissInputStream(InputStream in) {
        super(in);
    }

    @Override
    public Packet readPacket() {
        try {
            KissFrame kissFrame;
            while ((kissFrame = readFrame()) != null) {
                if (!kissFrame.isValid()) {
                    continue;
                }
                try {
                    return kissPacketRepresentation.toPacket(kissFrame.getData());
                } catch (Exception e) {
                    log.error("Error while deserializing packet", e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while reading frame", e);
        }
        return null;
    }

    public KissFrame readFrame() throws IOException {
        int b = 0;
        while ((b = in.read()) != -1) {
            if (b == KissConsts.FRAME_END) {
                if (!reading) {
                    reading = true;
                    buffer.reset();
                } else if (buffer.size() > 0) {
                    break;
                } else {
                    buffer.reset();
                }
            } else if (b == KissConsts.ESCAPE) {
                escaped = true;
            } else if (b == KissConsts.ESCAPED_FRAME_END) {
                if (escaped) {
                    buffer.write(KissConsts.FRAME_END);
                    escaped = false;
                } else {
                    buffer.write(KissConsts.ESCAPED_FRAME_END);
                }
            } else if (b == KissConsts.ESCAPED_ESCAPE) {
                if (escaped) {
                    buffer.write(KissConsts.ESCAPE);
                    escaped = false;
                } else {
                    buffer.write(KissConsts.ESCAPED_ESCAPE);
                }
            } else {
                if (escaped) {
                    buffer.reset();
                    escaped = false;
                } else {
                    buffer.write(b);
                }
            }
        }

        if (b == -1) {
            throw new IOException("End of input stream");
        }

        KissFrame kissFrame = new KissFrame();

        if (buffer.size() == 0) {
            kissFrame.setValid(false);
            return kissFrame;
        }

        kissFrame.setValid(true);

        byte[] rawFrame = buffer.toByteArray();
        buffer.reset();

        int port = (rawFrame[0] >> 4) & 0x0f;
        kissFrame.setPort(port);

        int command = rawFrame[0] & 0x0f;
        kissFrame.setCommand(command);

        byte[] frameData = Arrays.copyOfRange(rawFrame, 1, rawFrame.length);
        kissFrame.setData(frameData);

        return kissFrame;
    }
}
