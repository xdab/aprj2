package pl.so5dz.aprj2.plugin.digipeater;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import pl.so5dz.aprj2.aprs.callsign.Callsign;
import pl.so5dz.aprj2.aprs.callsign.impl.DefaultCallsign;
import pl.so5dz.aprj2.aprs.packet.impl.DefaultPacket;

public class DigipeaterTest {
    private static Digipeater digipeater;

    @BeforeAll
    public static void setUp() {
        digipeater = Digipeater.builder()
                .ownCallsign(TestConstants.n0call)
                .tracedAliases(Set.of("WIDE"))
                .untracedAliases(Set.of("SP"))
                .build();
    }

    @Test
    public void handle_nullPacket_returnsNull() {
        var returnedPacket = assertDoesNotThrow(() -> digipeater.handle(null));
        assertNull(returnedPacket);
    }

    @Test
    public void testHandle_emptyPath_returnsNull() {
        var packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.aprs)
                .build();
        var returnedPacket = assertDoesNotThrow(() -> digipeater.handle(packet));
        assertNull(returnedPacket);
    }

    @Test
    public void testHandle_fullPath_returnsNull() {
        var path = new ArrayList<Callsign>();
        for (int i = 0; i < 8; i++) {
            path.add(TestConstants.sampleRepeater);
        }
        var packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.aprs)
                .path(path)
                .build();
        var returnedPacket = assertDoesNotThrow(() -> digipeater.handle(packet));
        assertNull(returnedPacket);
    }

    @Test
    public void testHandle_ownPacket_returnsNull() {
        var ownPacket = DefaultPacket.builder()
                .source(TestConstants.n0call)
                .destination(TestConstants.aprs)
                .path(List.of(TestConstants.sampleRepeater))
                .build();
        var returnedPacket = assertDoesNotThrow(() -> digipeater.handle(ownPacket));
        assertNull(returnedPacket);
    }

    @Test
    public void testHandle_packetAddressedToSelf_returnsNull() {
        var packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.n0call)
                .path(List.of(TestConstants.sampleRepeater))
                .build();
        var returnedPacket = assertDoesNotThrow(() -> digipeater.handle(packet));
        assertNull(returnedPacket);
    }

    @Test
    public void testHandle_packetRepeatedByThisDigipeater_returnsNull() {
        var packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.aprs)
                .path(List.of(DefaultCallsign.builder()
                        .base("N0CALL")
                        .repeated(true)
                        .build()))
                .build();
        var returnedPacket = assertDoesNotThrow(() -> digipeater.handle(packet));
        assertNull(returnedPacket);
    }

    @Test
    public void testHandle_noRepeatableAlias_returnsNull() {
        var packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.aprs)
                .path(List.of(TestConstants.sampleRepeater, TestConstants.wide1Exhausted, TestConstants.sp1Exhausted))
                .build();
        var returnedPacket = assertDoesNotThrow(() -> digipeater.handle(packet));
        assertNull(returnedPacket);
    }

    @Test
    public void testHandle_validTracedAlias_works() {
        var packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.aprs)
                .path(List.of(TestConstants.sampleRepeater, TestConstants.wide1Available, TestConstants.sampleRepeater))
                .control(1)
                .protocol(2)
                .info("Test")
                .build();
        var returnedPacket = assertDoesNotThrow(() -> digipeater.handle(packet));
        assertNotNull(returnedPacket);

        // Assert packet fields other than path were unmodified
        assertEquals(packet.getSource(), returnedPacket.getSource());
        assertEquals(packet.getDestination(), returnedPacket.getDestination());
        assertEquals(packet.getControl(), returnedPacket.getControl());
        assertEquals(packet.getProtocol(), returnedPacket.getProtocol());
        assertEquals(packet.getInfo(), returnedPacket.getInfo());

        var path = returnedPacket.getPath();
        assertNotNull(path);

        // Assert own callsign was added before the traced alias
        assertTrue(path.get(1).simpleEquals(TestConstants.n0callRepeated));

        // Assert the updated alias is present in the path
        assertTrue(path.get(2).simpleEquals(TestConstants.wide1Exhausted));
        assertTrue(path.get(2).isRepeated());

        // Assert callsigns before ownCallsign were not modified
        for (int i = 0; i < 1; i++) {
            assertEquals(TestConstants.sampleRepeater, path.get(i));
        }

        // Assert callsigns after the alias were not modified
        for (int i = 3; i < path.size(); i++) {
            assertEquals(TestConstants.sampleRepeater, path.get(i));
        }
    }

    @Test
    public void testHandle_validUntracedAlias_works() {
        var packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.aprs)
                .path(List.of(TestConstants.wide1Exhausted, TestConstants.sp1Available, TestConstants.wide1Exhausted))
                .control(1)
                .protocol(2)
                .info("Test")
                .build();
        var returnedPacket = assertDoesNotThrow(() -> digipeater.handle(packet));
        assertNotNull(returnedPacket);

        // Assert packet fields other than path were unmodified
        assertEquals(packet.getSource(), returnedPacket.getSource());
        assertEquals(packet.getDestination(), returnedPacket.getDestination());
        assertEquals(packet.getControl(), returnedPacket.getControl());
        assertEquals(packet.getProtocol(), returnedPacket.getProtocol());
        assertEquals(packet.getInfo(), returnedPacket.getInfo());

        var path = returnedPacket.getPath();
        assertNotNull(path);

        // Assert the updated alias is present in the path
        assertTrue(path.get(1).simpleEquals(TestConstants.sp1Exhausted));
        assertTrue(path.get(1).isRepeated());

        // Assert callsigns before alias were not modified
        for (int i = 0; i < 1; i++) {
            assertEquals(TestConstants.wide1Exhausted, path.get(i));
        }

        // Assert callsigns after the alias were not modified
        for (int i = 2; i < path.size(); i++) {
            assertEquals(TestConstants.wide1Exhausted, path.get(i));
        }
    }

    @Test
    public void testHandle_validExplicitPath_works() {
        var packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.aprs)
                .path(List.of(TestConstants.n0call, TestConstants.sampleRepeater))
                .control(1)
                .protocol(2)
                .info("Test")
                .build();
        var returnedPacket = assertDoesNotThrow(() -> digipeater.handle(packet));
        assertNotNull(returnedPacket);

        // Assert packet fields other than path were unmodified
        assertEquals(packet.getSource(), returnedPacket.getSource());
        assertEquals(packet.getDestination(), returnedPacket.getDestination());
        assertEquals(packet.getControl(), returnedPacket.getControl());
        assertEquals(packet.getProtocol(), returnedPacket.getProtocol());
        assertEquals(packet.getInfo(), returnedPacket.getInfo());

        var path = returnedPacket.getPath();
        assertNotNull(path);

        // Assert the updated alias is present in the path
        int aliasIndex = path.indexOf(TestConstants.n0callRepeated);
        assertNotEquals(-1, aliasIndex);
        var alias = path.get(aliasIndex);
        assertTrue(alias.isRepeated());

        // Assert callsigns before alias were not modified
        for (int i = 0; i < aliasIndex; i++) {
            assertEquals(TestConstants.sampleRepeater, path.get(i));
        }

        // Assert callsigns after the alias were not modified
        for (int i = aliasIndex + 1; i < path.size(); i++) {
            assertEquals(TestConstants.sampleRepeater, path.get(i));
        }
    }
}
