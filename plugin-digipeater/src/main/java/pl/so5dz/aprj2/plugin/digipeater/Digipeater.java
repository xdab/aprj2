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
import pl.so5dz.aprj2.aprs.models.Packet;

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

    public Digipeater(Callsign ownCallsign, Collection<String> tracedAliases, Collection<String> untracedAliases) {
        this.ownCallsign = ownCallsign;
        this.tracedAliases = new HashSet<>(tracedAliases);
        this.untracedAliases = new HashSet<>(untracedAliases);
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

        // Find first alias which is grounds for repeating the packet
        Callsign alias = packet.getPath()
                .stream()
                .filter(this::isRepeatable)
                .findFirst()
                .orElse(null);
        if (alias == null) {
            return null;
        }

        // Update the alias to mark packet repetition
        Callsign updatedAlias = isOwnCallsign(alias) ? repeatedOwnCallsign() : decrement(alias);
        List<Callsign> newPath = new ArrayList<>(packet.getPath());
        int aliasIndex = newPath.indexOf(alias);
        newPath.set(aliasIndex, updatedAlias);
        if (isTracedAlias(alias)) {
            // Add own callsign before the traced alias
            newPath.add(aliasIndex, repeatedOwnCallsign());
        }

        return Packet.builder()
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
        return ownCallsign = Callsign.builder()
                .base(ownCallsign.getBase())
                .ssid(ownCallsign.getSsid())
                .repeated(true)
                .build();
    }

    private Callsign decrement(Callsign callsign) {
        int decrementedSsid = Math.max(0, callsign.getSsid() - 1);
        return Callsign.builder()
                .base(callsign.getBase())
                .ssid(decrementedSsid)
                .repeated(decrementedSsid == 0) // Mark as repeated when all desired hops were performed
                .build();
    }

    private boolean isRepeatable(Callsign alias) {
        boolean hasNotBeenRepeated = !alias.isRepeated();
        boolean isOwnCallsign = isOwnCallsign(alias);
        boolean hasRemainingHops = alias.getSsid() > 0;
        boolean isValidAlias = hasRemainingHops && (isTracedAlias(alias) || isUntracedAlias(alias));
        return hasNotBeenRepeated && (isValidAlias || isOwnCallsign);
    }

    private boolean isOwnCallsign(Callsign callsign) {
        return ownCallsign.simpleEquals(callsign);
    }

    private boolean isTracedAlias(Callsign callsign) {
        return (tracedAliases != null) && tracedAliases.contains(callsign.getBase());
    }

    private boolean isUntracedAlias(Callsign callsign) {
        return (untracedAliases != null) && untracedAliases.contains(callsign.getBase());
    }
}
