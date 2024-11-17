package pl.so5dz.aprj2.protocol.tnc2;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.representation.impl.Tnc2Representation;
import pl.so5dz.aprj2.protocol.PacketOutputStream;

@Slf4j
public class Tnc2OutputStream extends PacketOutputStream {
    private static final Tnc2Representation repr = Tnc2Representation.getInstance();

    public Tnc2OutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    public void writePacket(@NonNull Packet packet) {
        String packetString = repr.toRepresentation(packet);
        byte[] packetBytes = packetString.getBytes(StandardCharsets.US_ASCII);
        try {
            out.write(packetBytes);
            out.write((byte) '\r');
            out.write((byte) '\n');
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error while writing frame", e);
        }
    }
}
