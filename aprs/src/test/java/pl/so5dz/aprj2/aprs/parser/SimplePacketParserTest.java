package pl.so5dz.aprj2.aprs.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.parser.wip.SimplePacketParser;

public class SimplePacketParserTest {
    private static SimplePacketParser packetParser;

    @BeforeAll
    public static void setUp() {
        packetParser = new SimplePacketParser();
    }

    @Test
    public void parse_inputNull_returnNull() {
        Packet packet = packetParser.parse(null);
        assertNull(packet);
    }

    @Test
    public void parse_inputEmpty_returnNull() {
        Packet packet = packetParser.parse("");
        assertNull(packet);
    }

    @Test
    public void parse_inputBlank_returnNull() {
        Packet packet = packetParser.parse("\t   \t \n");
        assertNull(packet);
    }

    @Test
    public void parse_simpleInput_works() {
        Packet packet = packetParser.parse("N0CALL>APRS:valid");
        assertNotNull(packet);
        Callsign source = packet.getSource();
        assertNotNull(source);
        assertEquals("N0CALL", source.getBase());
        assertEquals(0, source.getSsid());
        assertEquals(false, source.isRepeated());
        Callsign destination = packet.getDestination();
        assertNotNull(destination);
        assertEquals("APRS", destination.getBase());
        assertEquals(0, destination.getSsid());
        assertEquals(false, destination.isRepeated());
        assertEquals("valid", packet.getInfo());
    }

    @Test
    public void parse_multipleSeparatedPackets_returnsFirst() {
        Packet packet = packetParser.parse("N0CALL>APRS:first\nN0CALL>APRS:second");
        assertNotNull(packet);
        assertEquals("first", packet.getInfo());
    }
}
