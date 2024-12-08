package pl.so5dz.aprj2.plugin.digipeater;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.DefaultCallsign;
import pl.so5dz.aprj2.aprs.models.DefaultPacket;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.models.RepeatingScheme;

@Getter
@Builder
@ToString
public class Digipeater {
    /**
     * Callsign of the digipeater.
     */
    private Callsign ownCallsign;

    /**
     * Set of traced aliases that handled by this digipeater.
     * <p>
     * Most commonly WIDEn or TRACE.
     * </p>
     */
    private final Set<String> tracedAliases;

    /**
     * Set of untraced aliases that handled by this digipeater.
     * <p>
     * Most commonly local, regional or national aliases, for example: SP1, ND2.
     * </p>
     */
    private final Set<String> untracedAliases;

    /**
     * Helper object: set of all aliases handled by this digipeater.
     */
    private final Set<String> allHandledAliases = new HashSet<>();

    public Digipeater(Callsign ownCallsign, Collection<String> tracedAliases, Collection<String> untracedAliases) {
        this.ownCallsign = ownCallsign;
        this.tracedAliases = new HashSet<>(tracedAliases);
        this.untracedAliases = new HashSet<>(untracedAliases);
        // allHandledAliases = tracedAliases UNION untracedAliases
        this.allHandledAliases.addAll(tracedAliases);
        this.allHandledAliases.addAll(untracedAliases);
    }

    /**
     * Handles the packet by creating a digipeated copy of it, if applicable.
     *
     * @param packet packet to handle
     * @return packet to transmit or null if the packet should not be repeated
     */
    public Packet handle(Packet packet) {
        if (packet == null) {
            return null;
        }

        boolean isPathEmpty = packet.getPath().isEmpty();
        if (isPathEmpty) {
            return null;
        }

        boolean isPathFull = packet.getPath().size() >= 8;
        if (isPathFull) {
            return null;
        }

        boolean isSentByThisDigipeater = isOwnCallsign(packet.getSource());
        if (isSentByThisDigipeater) {
            return null;
        }

        boolean isAddressedToThisDigipeater = isOwnCallsign(packet.getDestination());
        if (isAddressedToThisDigipeater) {
            return null;
        }

        // Find first applicable repeating instruction
        Callsign repeatingInstruction = packet.getPath()
                .stream()
                .filter(this::isApplicableRepeatingInstruction)
                .findFirst()
                .orElse(null);
        if (repeatingInstruction == null) {
            return null;
        }

        // Prepare new path marking the performed digipeating
        List<Callsign> newPath = new ArrayList<>(packet.getPath());
        int instructionIndex = newPath.indexOf(repeatingInstruction);
        if (isOwnCallsign(repeatingInstruction)) {
            Callsign updatedInstruction = repeatedOwnCallsign();
            newPath.set(instructionIndex, updatedInstruction);
        } else {
            RepeatingScheme scheme = new RepeatingScheme(repeatingInstruction);
            newPath.set(instructionIndex, scheme.decrement());
            if (isTracedAlias(scheme.getAlias())) {
                newPath.add(instructionIndex, repeatedOwnCallsign());
            }
        }

        return DefaultPacket.builder()
                .source(packet.getSource())
                .destination(packet.getDestination())
                .path(newPath)
                .control(packet.getControl())
                .protocol(packet.getProtocol())
                .info(packet.getInfo())
                .build();
    }

    private Callsign repeatedOwnCallsign() {
        if (ownCallsign.isRepeated()) {
            return ownCallsign;
        }
        return ownCallsign = DefaultCallsign.builder()
                .base(ownCallsign.getBase())
                .ssid(ownCallsign.getSsid())
                .repeated(true)
                .build();
    }

    private boolean isApplicableRepeatingInstruction(Callsign callsign) {
        if (callsign.isRepeated()) {
            return false;
        }
        if (isOwnCallsign(callsign)) {
            return true;
        }
        // Try parsing a repeating scheme out of the callsign
        RepeatingScheme scheme;
        try {
            scheme = new RepeatingScheme(callsign);
        } catch (AssertionError e) {
            // If not a repeating scheme, ignore the callsign
            return false;
        }
        if (scheme.isExhausted()) {
            return false;
        }
        return isHandledAlias(scheme.getAlias());
    }

    private boolean isOwnCallsign(Callsign callsign) {
        return ownCallsign.simpleEquals(callsign);
    }

    private boolean isTracedAlias(String alias) {
        return (tracedAliases != null) && tracedAliases.contains(alias);
    }

    private boolean isHandledAlias(String alias) {
        return (allHandledAliases != null) && allHandledAliases.contains(alias);
    }
}
