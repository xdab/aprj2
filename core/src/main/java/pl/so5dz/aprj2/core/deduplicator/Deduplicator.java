package pl.so5dz.aprj2.core.deduplicator;

import java.util.HashMap;
import java.util.Map;

import pl.so5dz.aprj2.aprs.models.Packet;

public class Deduplicator {
    private static final int REMOVE_OLD_ENTRIES_EVERY_PACKETS = 100;

    private final Map<Integer, Long> packetTransmissionTimes;
    private final long deduplicationWindowMillis;
    private int packetCounter = 0;

    public Deduplicator(long deduplicationWindowMillis) {
        this.deduplicationWindowMillis = deduplicationWindowMillis;
        packetTransmissionTimes = new HashMap<>();
    }

    /**
     * Deduplicates the packet based on its simple hash code.
     * 
     * @param packet a packet
     * @return the input packet or null if the packet is a duplicate
     */
    public Packet deduplicate(Packet packet) {
        if (packet == null) {
            return null;
        }
        long now = System.currentTimeMillis();
        removeOldEntries(now);
        int packetHashCodeIgnoringPath = packet.simpleHashCode();
        long lastTransmitted = packetTransmissionTimes.getOrDefault(packetHashCodeIgnoringPath, 0L);
        long timeSinceLastTransmission = now - lastTransmitted;
        if (timeSinceLastTransmission > deduplicationWindowMillis) {
            packetTransmissionTimes.put(packetHashCodeIgnoringPath, now);
            return packet;
        }
        return null;
    }

    private void removeOldEntries(long now) {
        if (packetCounter++ >= REMOVE_OLD_ENTRIES_EVERY_PACKETS) {
            packetTransmissionTimes.entrySet().removeIf(entry -> {
                long timeSinceLastTransmission = now - entry.getValue();
                return timeSinceLastTransmission > deduplicationWindowMillis;
            });
            packetCounter = 0;
        }
    }
}
