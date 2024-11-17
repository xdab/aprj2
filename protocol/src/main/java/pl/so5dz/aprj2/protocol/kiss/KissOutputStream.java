package pl.so5dz.aprj2.protocol.kiss;

import java.io.OutputStream;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.representation.impl.Ax25Representation;
import pl.so5dz.aprj2.protocol.PacketOutputStream;

@Slf4j
public class KissOutputStream extends PacketOutputStream {
    private static final Ax25Representation kissPacketRepresentation = Ax25Representation.getInstance();

    public KissOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void writePacket(@NonNull Packet packet) {
        byte[] packetBytes = kissPacketRepresentation.toRepresentation(packet);

        KissFrame kissFrame = new KissFrame();
        kissFrame.setCommand(0);
        kissFrame.setPort(0);
        kissFrame.setData(packetBytes);
        kissFrame.setValid(packetBytes != null && packetBytes.length > 0);

        writeFrame(kissFrame);
    }

    public void writeFrame(@NonNull KissFrame kissFrame) {
        if (!kissFrame.isValid()) {
            log.warn("Trying to write invalid frame");
            return;
        }

        int commandPort = (kissFrame.getPort() << 4) & 0xF0;
        commandPort |= kissFrame.getCommand() & 0x0F;

        try {
            out.write(KissConsts.FRAME_END);
            out.write(commandPort);
            byte[] data = kissFrame.getData();
            for (byte b : data) {
                if (b == KissConsts.FRAME_END) {
                    out.write(KissConsts.ESCAPE);
                    out.write(KissConsts.ESCAPED_FRAME_END);
                } else if (b == KissConsts.ESCAPE) {
                    out.write(KissConsts.ESCAPE);
                    out.write(KissConsts.ESCAPED_ESCAPE);
                } else {
                    out.write(b);
                }
            }
            out.write(KissConsts.FRAME_END);
        } catch (Exception e) {
            log.error("Error while writing frame", e);
            throw new RuntimeException("Error while writing frame", e);
        }
    }
}
