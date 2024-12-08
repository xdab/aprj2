package pl.so5dz.aprj2.protocol.tnc2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.packet.Packet;
import pl.so5dz.aprj2.aprs.representation.impl.Tnc2Representation;
import pl.so5dz.aprj2.protocol.PacketInputStream;

@Slf4j
public class Tnc2InputStream extends PacketInputStream {
    private static final Tnc2Representation repr = Tnc2Representation.getInstance();

    private final BufferedReader reader;

    public Tnc2InputStream(InputStream inputStream) {
        super(inputStream);
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public Packet readPacket() {
        String packetString = readLine();
        if (packetString == null || packetString.isBlank()) {
            return null;
        }
        if (packetString.startsWith("#")) {
            log.debug(packetString);
            return null;
        }
        return repr.toPacket(packetString);
    }

    private String readLine() {
        try {
            String line = reader.readLine();
            if (line == null) {
                reader.close();
            }
            return line;
        } catch (IOException e) {
            throw new RuntimeException("Error while reading line", e);
        }
    }
}
