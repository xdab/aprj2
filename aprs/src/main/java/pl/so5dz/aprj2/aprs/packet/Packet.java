package pl.so5dz.aprj2.aprs.packet;

import java.util.List;
import java.util.Objects;

import pl.so5dz.aprj2.aprs.callsign.Callsign;
import pl.so5dz.aprj2.aprs.representation.impl.Tnc2Representation;

public abstract class Packet {

    /**
     * The callsign of the station that originated the packet.
     */
    public abstract Callsign getSource();

    /**
     * The callsign of the station that is the intended recipient of the packet.
     * <p>
     * For APRS, this has a default value of "APRJ2".
     * </p>
     */
    public abstract Callsign getDestination();

    /**
     * The path of the packet, which is a list of callsigns of stations
     * that have repeated or are supposed to repeat the packet.
     */
    public abstract List<Callsign> getPath();

    /**
     * The control field of the packet.
     * <p>
     * For APRS, this has a fixed value of 0x03.
     * </p>
     */
    public abstract int getControl();

    /**
     * The protocol ID of the packet.
     * <p>
     * For APRS, this has a fixed value of 0xf0.
     * </p>
     */
    public abstract int getProtocol();

    /**
     * The information field of the packet, which contains the actual data.
     */
    public abstract String getInfo();

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
        return Objects.hash(getSource(), getDestination(), getInfo());
    }
}
