package pl.so5dz.aprj2.aprs.constants;

import lombok.experimental.UtilityClass;
import pl.so5dz.aprj2.aprs.models.Callsign;

/**
 * Common callsigns and callsign-like indicators seen in real-world APRS
 * packets.
 */
@UtilityClass
public class Callsigns {

    /**
     * Callsign-like indicator of APRJ2.
     */
    public static final Callsign APRJ2 = Callsign.builder().base("APRJ2").build();

    /**
     * Callsign-like indicator of TCP/IP usage somewhere in the packet's travel.
     */
    public static final Callsign TCPIP = Callsign.builder().base("TCPIP").build();

    /**
     * Callsign-like q-Construct for a packet received by an iGate.
     * <p>
     * In packet path, this callsign must be followed by the iGate's callsign.
     * <p>
     */
    public static final Callsign qAR = Callsign.builder().base("qAR").build();

    /*
     * Common placeholder for a non-existent callsign.
     */
    public static final Callsign N0CALL = Callsign.builder().base("N0CALL").build();

    /**
     * Callsign-like indicator of a packet that should not be gated and remain
     * RF-only.
     */
    public static final Callsign NOGATE = Callsign.builder().base("NOGATE").build();

    /**
     * Callsign-like indicator of a packet that should not be gated and remain
     * RF-only.
     */
    public static final Callsign RFONLY = Callsign.builder().base("RFONLY").build();

}
