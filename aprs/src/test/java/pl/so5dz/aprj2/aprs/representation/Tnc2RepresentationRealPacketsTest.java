package pl.so5dz.aprj2.aprs.representation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.representation.impl.Tnc2Representation;

public class Tnc2RepresentationRealPacketsTest {
    private static final Tnc2Representation TNC2_REPRESENTATION = Tnc2Representation.getInstance();
    private static final String PACKETS_FILE_NAME = "packets.txt";

    @Test
    void testOnRealPackets() {
        Packet packet;
        for (String rawPacket : rawTnc2Packets()) {
            packet = assertDoesNotThrow(() -> TNC2_REPRESENTATION.toPacket(rawPacket));
            assertNotNull(packet.getSource());
            assertNotNull(packet.getDestination());
            assertNotNull(packet.getInfo());
        }
    }

    private static List<String> rawTnc2Packets() {
        ClassLoader classLoader = Tnc2RepresentationRealPacketsTest.class.getClassLoader();
        try {
            Path packetsFilePath = Path.of(classLoader.getResource(PACKETS_FILE_NAME).toURI());
            return Files.lines(packetsFilePath).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
