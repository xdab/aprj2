package pl.so5dz.aprj2.aprs.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import pl.so5dz.aprj2.aprs.constants.Callsigns;
import pl.so5dz.aprj2.aprs.representation.impl.Tnc2Representation;

/**
 * Represents a single AX.25 packet.
 */
@Data
@Builder
public class Packet {
    /**
     * The callsign of the station that originated the packet.
     */
    @NonNull
    private Callsign source;

    /**
     * The callsign of the station that is the intended recipient of the packet.
     * <p>
     * For APRS, this has a default value of "APRJ2".
     * </p>
     */
    @NonNull
    @Builder.Default
    private Callsign destination = Callsigns.APRJ2;

    /**
     * The path of the packet, which is a list of callsigns of stations
     * that have repeated or are supposed to repeat the packet.
     */
    @NonNull
    @Builder.Default
    private List<Callsign> path = new ArrayList<>();

    /**
     * The control field of the packet.
     * <p>
     * For APRS, this has a fixed value of 0x03.
     * </p>
     */
    @Builder.Default
    private int control = 0x03;

    /**
     * The protocol ID of the packet.
     * <p>
     * For APRS, this has a fixed value of 0xf0.
     * </p>
     */
    @Builder.Default
    private int protocol = 0xf0;

    /**
     * The information field of the packet, which contains the actual data.
     */
    private String info;

    /**
     * Returns the string representation of the packet in TNC2 format.
     * 
     * @return the TNC2 representation of the packet
     */
    @Override
    public String toString() {
        return Tnc2Representation.getInstance().toRepresentation(this);
    }

    /**
     * Returns a simple hash code of the packet.
     * <p>
     * This hash code is based on the hash codes of the source, destination, and
     * info fields. It can be used to detect duplicate packets that differ at most
     * in the path. Control and protocol fields are also ignored, as they are
     * fixed for APRS.
     * </p>
     *
     * @return the simple hash code of the packet
     */
    public int simpleHashCode() {
        return Objects.hash(source.simpleHashCode(), destination.simpleHashCode(), info);
    }
}