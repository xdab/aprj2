package pl.so5dz.aprj2.core.deduplicator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import pl.so5dz.aprj2.aprs.packet.Packet;
import pl.so5dz.aprj2.aprs.packet.impl.DefaultPacket;
import pl.so5dz.aprj2.core.TestConstants;

public class DeduplicatorTest {

    private static final long DEDUPLICATION_WINDOW_MILLIS = 100;

    private static Deduplicator deduplicator;

    @BeforeAll
    public static void setUp() {
        deduplicator = new Deduplicator(DEDUPLICATION_WINDOW_MILLIS);
    }

    @Test
    public void deduplicate_inputNull_returnsNull() {
        Packet result = assertDoesNotThrow(() -> deduplicator.deduplicate(null));
        assertNull(result);
    }

    @Test
    public void deduplicate_inputNotNull_forTheFirstTime_returnsInput() {
        Packet packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.sampleStation)
                .info("Test")
                .build();
        Packet result = assertDoesNotThrow(() -> deduplicator.deduplicate(packet));
        assertEquals(packet, result);
    }

    @Test
    public void deduplicate_inputNotNull_forTheSecondTime_returnsNull() {
        Packet packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.sampleStation)
                .info("Test")
                .build();
        assertDoesNotThrow(() -> deduplicator.deduplicate(packet));
        Packet result = assertDoesNotThrow(() -> deduplicator.deduplicate(packet));
        assertNull(result);
    }

    @Test
    public void deduplicate_inputNotNull_afterDeduplicationWindow_returnsInput() {
        Packet packet = DefaultPacket.builder()
                .source(TestConstants.sampleStation)
                .destination(TestConstants.sampleStation)
                .info("Test")
                .build();
        assertDoesNotThrow(() -> deduplicator.deduplicate(packet));
        try {
            Thread.sleep(DEDUPLICATION_WINDOW_MILLIS + 10);
        } catch (InterruptedException e) {
            fail("Interrupted while sleeping");
        }
        Packet result = assertDoesNotThrow(() -> deduplicator.deduplicate(packet));
        assertEquals(packet, result);
    }
}
