package pl.so5dz.aprj2.aprs.representation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.representation.impl.Tnc2Representation;

public class Tnc2RepresentationTest {
    private final Tnc2Representation instance = Tnc2Representation.getInstance();

    @Test
    public void testToRepresentation1() {
        Packet packet = Packet.builder()
                .source(Callsign.builder().base("ABC").ssid(1).build())
                .destination(Callsign.builder().base("DEF").build())
                .info("INFO1")
                .build();
        String repr = instance.toRepresentation(packet);
        assertEquals("ABC-1>DEF:INFO1", repr);
    }

    @Test
    public void testToRepresentation2() {
        Packet packet = Packet.builder()
                .source(Callsign.builder().base("ABC").build())
                .destination(Callsign.builder().base("DEF").ssid(15).build())
                .path(List.of(
                        Callsign.builder().base("GHI").ssid(8).repeated(true).build(),
                        Callsign.builder().base("JKL").repeated(true).build()))
                .info("INFO 2")
                .build();
        String repr = instance.toRepresentation(packet);
        assertEquals("ABC>DEF-15,GHI-8*,JKL*:INFO 2", repr);
    }

    @Test
    public void testToPacket1() {
        Packet packet = instance.toPacket("ABC-1>DEF:INFO1");

        Callsign source = packet.getSource();
        assertNotNull(source);
        assertEquals("ABC", source.getBase());
        assertEquals(1, source.getSsid());
        assertFalse(source.isRepeated());

        Callsign destination = packet.getDestination();
        assertNotNull(destination);
        assertEquals("DEF", destination.getBase());
        assertEquals(0, destination.getSsid());
        assertFalse(destination.isRepeated());

        List<Callsign> path = packet.getPath();
        assertNotNull(path);
        assertEquals(0, path.size());

        assertEquals("INFO1", packet.getInfo());
    }

    @Test
    public void testToPacket2() {
        Packet packet = instance.toPacket("ABC>DEF-15,GHI-8*,JKL*:INFO 2");

        Callsign source = packet.getSource();
        assertNotNull(source);
        assertEquals("ABC", source.getBase());
        assertEquals(0, source.getSsid());
        assertFalse(source.isRepeated());

        Callsign destination = packet.getDestination();
        assertNotNull(destination);
        assertEquals("DEF", destination.getBase());
        assertEquals(15, destination.getSsid());
        assertFalse(destination.isRepeated());

        List<Callsign> path = packet.getPath();
        assertNotNull(path);
        assertEquals(2, path.size());

        Callsign path0 = path.get(0);
        assertNotNull(path0);
        assertEquals("GHI", path0.getBase());
        assertEquals(8, path0.getSsid());
        assertTrue(path0.isRepeated());

        Callsign path1 = path.get(1);
        assertNotNull(path1);
        assertEquals("JKL", path1.getBase());
        assertEquals(0, path1.getSsid());
        assertTrue(path1.isRepeated());

        assertEquals("INFO 2", packet.getInfo());
    }

    @Test
    public void testToCallsign1() {
        Callsign callsign = instance.toCallsign("ABC-1");
        assertNotNull(callsign);
        assertEquals("ABC", callsign.getBase());
        assertEquals(1, callsign.getSsid());
        assertFalse(callsign.isRepeated());
    }

    @Test
    public void testToCallsign2() {
        Callsign callsign = instance.toCallsign("DEF-15*");
        assertNotNull(callsign);
        assertEquals("DEF", callsign.getBase());
        assertEquals(15, callsign.getSsid());
        assertTrue(callsign.isRepeated());
    }

    @Test
    public void testToCallsign3() {
        Callsign callsign = instance.toCallsign("GHI");
        assertNotNull(callsign);
        assertEquals("GHI", callsign.getBase());
        assertEquals(0, callsign.getSsid());
        assertFalse(callsign.isRepeated());
    }

    @Test
    public void testToRepresentationCallsign1() {
        Callsign callsign = Callsign.builder().base("ABC").ssid(1).build();
        String repr = instance.toRepresentation(callsign);
        assertEquals("ABC-1", repr);
    }

    @Test
    public void testToRepresentationCallsign2() {
        Callsign callsign = Callsign.builder().base("DEF").ssid(15).repeated(true).build();
        String repr = instance.toRepresentation(callsign);
        assertEquals("DEF-15*", repr);
    }

    @Test
    public void testToRepresentationCallsign3() {
        Callsign callsign = Callsign.builder().base("GHI").build();
        String repr = instance.toRepresentation(callsign);
        assertEquals("GHI", repr);
    }
}
