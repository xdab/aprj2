package pl.so5dz.aprj2.plugin.digipeater.multipeater;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import pl.so5dz.aprj2.aprs.packet.impl.DefaultPacket;
import pl.so5dz.aprj2.plugin.digipeater.Digipeater;
import pl.so5dz.aprj2.plugin.digipeater.TestConstants;

public class MultipeaterTest {

    private Digipeater digipeater1; // mock
    private Digipeater digipeater2; // mock
    private Digipeater digipeater3; // mock
    private Multipeater multipeater;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(MultipeaterTest.class);
        digipeater1 = mock(Digipeater.class);
        digipeater2 = mock(Digipeater.class);
        digipeater3 = mock(Digipeater.class);
        MultipeaterEntry multipeaterEntry1 = new MultipeaterEntry("source1", "target1", digipeater1);
        MultipeaterEntry multipeaterEntry2 = new MultipeaterEntry("source2", "target2", digipeater2);
        MultipeaterEntry multipeaterEntry3 = new MultipeaterEntry("source1", "target3", digipeater3);
        multipeater = new Multipeater(List.of(multipeaterEntry1, multipeaterEntry2, multipeaterEntry3));
    }

    @Test
    public void testHandle_nullPacket_returnsNull() {
        var returnedPackets = assertDoesNotThrow(() -> multipeater.handle(null, "source1"));
        assertNull(returnedPackets);
    }

    @Test
    public void testHandle_nullSourceDeviceName_returnsNull() {
        var packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.aprs)
                .build();
        var returnedPackets = assertDoesNotThrow(() -> multipeater.handle(packet, null));
        assertNull(returnedPackets);
    }

    @Test
    public void testHandle_noMatchingDigipeaters_returnsNull() {
        var packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.aprs)
                .build();
        var returnedPackets = assertDoesNotThrow(() -> multipeater.handle(packet, "skdfhjgskdjfhg"));
        assertNull(returnedPackets);
    }

    @Test
    public void testHandle_singleMatchingDigipeater_callsItsHandle() {
        var packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.aprs)
                .build();
        doReturn(packet).when(digipeater2).handle(packet);
        List<MultipeatedPacket> returnedPackets = assertDoesNotThrow(() -> multipeater.handle(packet, "source2"));
        verify(digipeater2).handle(packet);
        assertNotNull(returnedPackets);
        assertEquals(1, returnedPackets.size());
        MultipeatedPacket returnedPacket = returnedPackets.get(0);
        assertEquals("target2", returnedPacket.getTargetDeviceName());
        assertEquals(packet, returnedPacket.getPacket());
    }

    @Test
    public void testHandle_multipleMatchingDigipeaters_callsTheirHandles() {
        var packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.aprs)
                .build();
        doReturn(packet).when(digipeater1).handle(packet);
        doReturn(packet).when(digipeater3).handle(packet);
        var returnedPackets = assertDoesNotThrow(() -> multipeater.handle(packet, "source1"));
        verify(digipeater1).handle(packet);
        verify(digipeater3).handle(packet);
        assertNotNull(returnedPackets);
        assertEquals(2, returnedPackets.size());
        var returnedPacket = returnedPackets.get(0);
        assertEquals("target1", returnedPacket.getTargetDeviceName());
        assertEquals(packet, returnedPacket.getPacket());
        returnedPacket = returnedPackets.get(1);
        assertEquals("target3", returnedPacket.getTargetDeviceName());
        assertEquals(packet, returnedPacket.getPacket());
    }
}
