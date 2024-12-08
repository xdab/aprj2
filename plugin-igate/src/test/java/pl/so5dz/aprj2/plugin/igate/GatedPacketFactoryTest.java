package pl.so5dz.aprj2.plugin.igate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import pl.so5dz.aprj2.aprs.constants.Callsigns;
import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.DefaultCallsign;
import pl.so5dz.aprj2.aprs.models.DefaultPacket;
import pl.so5dz.aprj2.aprs.models.Packet;

public class GatedPacketFactoryTest {
    private static final Callsign sampleIGateCallsign = DefaultCallsign.builder().base("MYIGATE").build();
    private static final Packet samplePacket;
    static {
        Callsign source = DefaultCallsign.builder().base("AB1CD").ssid(1).build();
        Callsign destination = DefaultCallsign.builder().base("APRS").build();
        Callsign path1 = DefaultCallsign.builder().base("WIDE1").ssid(1).build();
        Callsign path2 = DefaultCallsign.builder().base("WIDE2").ssid(1).build();
        List<Callsign> path = List.of(path1, path2);
        samplePacket = DefaultPacket.builder()
                .source(source)
                .destination(destination)
                .path(path)
                .info("Hello, world!")
                .build();
    }

    @Test
    void buildRXIGatedPacket_invalidInput_fails() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> GatedPacketFactory.buildRXIGatedPacket(null, sampleIGateCallsign));
        assertThrows(NullPointerException.class,
                () -> GatedPacketFactory.buildRXIGatedPacket(samplePacket, null));
    }

    @Test
    void buildRXIGatedPacket_works() {
        // Act
        Packet gatedPacket = GatedPacketFactory.buildRXIGatedPacket(samplePacket, sampleIGateCallsign);

        // Assert
        // source, destination and info remain unchanged
        assertEquals(samplePacket.getSource(), gatedPacket.getSource());
        assertEquals(samplePacket.getDestination(), gatedPacket.getDestination());
        assertEquals("Hello, world!", gatedPacket.getInfo());
        // path is extended by two callsigns:
        assertEquals(4, gatedPacket.getPath().size());
        assertEquals(samplePacket.getPath().get(0), gatedPacket.getPath().get(0));
        assertEquals(samplePacket.getPath().get(1), gatedPacket.getPath().get(1));
        // 1) qAR construct
        assertEquals(Callsigns.qAR, gatedPacket.getPath().get(2));
        // 2) iGate callsign
        assertEquals(sampleIGateCallsign, gatedPacket.getPath().get(3));
    }

    @Test
    void buildTXIGatedPacket_invalidInput_fails() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> GatedPacketFactory.buildTXIGatedPacket(null, sampleIGateCallsign));
        assertThrows(NullPointerException.class,
                () -> GatedPacketFactory.buildTXIGatedPacket(samplePacket, null));
    }

    @Test
    void buildTXIGatedPacket_works() {
        // Act
        Packet gatedPacket = GatedPacketFactory.buildTXIGatedPacket(samplePacket, sampleIGateCallsign);

        // Assert
        // source and destination are transformed into IGate>APRJ2
        assertEquals(sampleIGateCallsign, gatedPacket.getSource());
        assertEquals(Callsigns.APRJ2, gatedPacket.getDestination());
        // path is empty
        assertNotNull(gatedPacket.getPath());
        assertEquals(0, gatedPacket.getPath().size());
        // info is the original packet, without path, prefixed with a special indicator
        assertEquals("}AB1CD-1>APRS,TCPIP:Hello, world!", gatedPacket.getInfo());
    }
}
