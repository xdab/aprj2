package pl.so5dz.aprj2.plugin.digipeater.multipeater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.models.Packet;

@Slf4j
public class Multipeater {
    /**
     * Mapping from device name to a list of digipeaters
     * having that device as a source.
     */
    private final Map<String, List<MultipeaterEntry>> entryMap;

    /**
     * Creates a new Multipeater from a list of digipeaters.
     * 
     * @param digipeaters list of digipeaters
     */
    public Multipeater(List<MultipeaterEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            throw new IllegalArgumentException("Multipeater must have at least one entry/digipeater");
        }
        // Group digipeaters by source device name
        // in order not to iterate over them for each packet
        this.entryMap = entries.stream()
                .collect(Collectors.groupingBy(
                        MultipeaterEntry::getSourceDeviceName,
                        Collectors.toList()));
    }

    /**
     * Handles a packet received from a specific device.
     * 
     * @param packet           the received packet
     * @param sourceDeviceName name of the device from which the packet was received
     * @return list of outbound packets or null if none are to be transmitted
     */
    public List<MultipeatedPacket> handle(Packet packet, String sourceDeviceName) {
        // Warn and return null immidiately if either input parameter is null
        if (packet == null) {
            log.warn("Null packet passed to Multipeater.handle");
            return null;
        }
        if (sourceDeviceName == null) {
            log.warn("Null sourceDeviceName passed to Multipeater.handle");
            return null;
        }

        // Get multipeater entries with the given source device,
        // if there are none, return null immediately
        List<MultipeaterEntry> matchingEntries = entryMap.get(sourceDeviceName);
        if (matchingEntries == null || matchingEntries.isEmpty()) {
            return null;
        }

        // Handle the packet with each digipeater, collect the results in a list
        List<MultipeatedPacket> outboundPackets = new ArrayList<>();
        for (MultipeaterEntry multipeaterEntry : matchingEntries) {
            Packet digipeatedPacket = multipeaterEntry.handle(packet);
            if (digipeatedPacket != null) {
                outboundPackets.add(new MultipeatedPacket(digipeatedPacket, multipeaterEntry.getTargetDeviceName()));
            }
        }

        // Return the list of packets to be transmitted
        return outboundPackets;
    }
}
