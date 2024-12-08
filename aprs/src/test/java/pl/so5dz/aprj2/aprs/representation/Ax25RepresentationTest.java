package pl.so5dz.aprj2.aprs.representation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.DefaultCallsign;
import pl.so5dz.aprj2.aprs.models.DefaultPacket;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.representation.impl.Ax25Representation;

public class Ax25RepresentationTest {
    private final Ax25Representation instance = Ax25Representation.getInstance();

    @Test
    public void testToRepresentation1() {
        Packet packet = DefaultPacket.builder()
                .source(DefaultCallsign.builder().base("ABC").ssid(1).build())
                .destination(DefaultCallsign.builder().base("DEF").build())
                .path(List.of())
                .info("INFO1")
                .control(0)
                .protocol(0)
                .build();
        byte[] repr = instance.toRepresentation(packet);
        byte[] expected = new byte[] {
                (byte) ('D' << 1), (byte) ('E' << 1), (byte) ('F' << 1),
                (byte) (' ' << 1), (byte) (' ' << 1), (byte) (' ' << 1), 0b01100000,
                (byte) ('A' << 1), (byte) ('B' << 1), (byte) ('C' << 1),
                (byte) (' ' << 1), (byte) (' ' << 1), (byte) (' ' << 1), 0b01100011,
                0, 0,
                'I', 'N', 'F', 'O', '1'
        };
        assertArrayEquals(expected, repr);
    }

    @Test
    public void testToPacket1() {
        byte[] representation = new byte[] {
                (byte) ('D' << 1), (byte) ('E' << 1), (byte) ('F' << 1),
                (byte) (' ' << 1), (byte) (' ' << 1), (byte) (' ' << 1), 0b01100000,
                (byte) ('A' << 1), (byte) ('B' << 1), (byte) ('C' << 1),
                (byte) (' ' << 1), (byte) (' ' << 1), (byte) (' ' << 1), 0b01100011,
                1, 2,
                'I', 'N', 'F', 'O', '1'
        };
        Packet packet = instance.toPacket(representation);
        assertNotNull(packet);

        Callsign destination = packet.getDestination();
        assertNotNull(destination);
        assertEquals("DEF", destination.getBase());
        assertEquals(0, destination.getSsid());
        assertFalse(destination.isRepeated());

        Callsign source = packet.getSource();
        assertNotNull(source);
        assertEquals("ABC", source.getBase());
        assertEquals(1, source.getSsid());
        assertFalse(source.isRepeated());

        assertEquals(1, packet.getControl());
        assertEquals(2, packet.getProtocol());
        assertEquals("INFO1", packet.getInfo());
    }

    @Test
    public void testToPacket2() {
        byte[] representation = new byte[] {
                (byte) 0x9C, (byte) 0x94, 0x6E, (byte) 0xA0, 0x40, 0x40, (byte) 0xE0,
                (byte) 0x9C, 0x6E, (byte) 0x98, (byte) 0x8A, (byte) 0x9A, 0x40, 0x60,
                (byte) 0x9C, 0x6E, (byte) 0x9E, (byte) 0x9E, 0x40, 0x40, (byte) 0xE3,
                0x3E, (byte) 0xF0
        };
        Packet packet = instance.toPacket(representation);
        assertNotNull(packet);

        Callsign destination = packet.getDestination();
        assertNotNull(destination);
        assertEquals("NJ7P", destination.getBase());
        assertEquals(0, destination.getSsid());
        assertTrue(destination.isRepeated());

        Callsign source = packet.getSource();
        assertNotNull(source);
        assertEquals("N7LEM", source.getBase());
        assertEquals(0, source.getSsid());
        assertFalse(source.isRepeated());

        List<Callsign> path = packet.getPath();
        assertNotNull(path);
        assertEquals(1, path.size());

        Callsign path0 = path.get(0);
        assertNotNull(path0);
        assertEquals("N7OO", path0.getBase());
        assertEquals(1, path0.getSsid());
        assertTrue(path0.isRepeated());

        assertEquals((byte) 0x3E, packet.getControl());
        assertEquals((byte) 0xF0, packet.getProtocol());
        assertNull(packet.getInfo());
    }
}
