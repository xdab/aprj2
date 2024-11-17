package pl.so5dz.aprj2.aprs.models;

import java.util.Objects;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import pl.so5dz.aprj2.aprs.representation.impl.Tnc2Representation;

/**
 * Callsign of an APRS station.
 * <p>
 * This class represents an APRS callsign, which consists of a base callsign,
 * an optional SSID and an optional flag indicating if the callsign acted
 * as a repeater.
 * </p>
 */
@Data
@Builder
public class Callsign {
    /**
     * Base callsign.
     * <p>
     * For a licensed amateur radio operator, this is the callsign
     * assigned by the regulatory authority.
     * </p>
     */
    @NonNull
    private String base;

    /**
     * SSID (Sub-Station IDentifier).
     * <p>
     * This is a number from 0 to 15 that can be used to differentiate
     * between multiple stations using the same base callsign.
     * </p>
     */
    private int ssid;

    /**
     * Flag indicating if the callsign acted as a repeater.
     * <p>
     * This flag is used in the context of APRS digipeating.
     * </p>
     */
    private boolean repeated;

    /**
     * Creates a new Callsign object from a string representation in TNC2 format.
     * 
     * @param tnc2Representation the TNC2 representation of the callsign
     * @return the Callsign object
     */
    public static Callsign fromString(String tnc2Representation) {
        return Tnc2Representation.getInstance().toCallsign(tnc2Representation);
    }

    /**
     * Returns the string representation of the callsign in TNC2 format.
     * 
     * @return the TNC2 representation of the callsign
     */
    @Override
    public String toString() {
        return Tnc2Representation.getInstance().toRepresentation(this);
    }

    /**
     * Compares the callsign with another object in a simplified way.
     * <p>
     * This method compares only the base callsign and SSID.
     * Repeated flag is not taken into account.
     * </p>
     * 
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    public boolean simpleEquals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof Callsign) {
            Callsign other = (Callsign) obj;
            return base.equalsIgnoreCase(other.base) && ssid == other.ssid;
        }
        return false;
    }

    /**
     * Calculates a simplified hash code for the callsign.
     * <p>
     * This hash code is based on the base callsign and SSID.
     * Repeated flag is not taken into account.
     * </p>
     * 
     * @return the simple hash code
     */
    public int simpleHashCode() {
        return Objects.hash(base, ssid);
    }

    /**
     * Checks if the callsign is a legal callsign, as in not a marker, q-construct,
     * name.
     * <p>
     * Examples of legal callsigns: N0CALL (unfortunately), SP1ABC, K1ABC-1, A1A.
     * </p>
     * <p>
     * Examples of non-legal callsigns: APRS, WIDE2-1, RELAY, TCPIP, qAR, RFONLY.
     * <p>
     */
    public boolean isLegalCallsign() {
        return base.matches("[A-Z]+[0-9]+[A-Z]+");
    }
}
