package pl.so5dz.aprj2.aprs.constants;

import lombok.experimental.UtilityClass;
import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.DefaultCallsign;

/**
 * Common callsigns and callsign-like indicators seen in real-world APRS
 * packets.
 */
@UtilityClass
public class Callsigns {

    /**
     * Callsign-like indicator of APRJ2.
     */
    public static final Callsign APRJ2 = DefaultCallsign.builder().base("APRJ2").build();

    /**
     * Callsign-like indicator of TCP/IP usage somewhere in the packet's travel.
     */
    public static final Callsign TCPIP = DefaultCallsign.builder().base("TCPIP").build();

    /**
     * Callsign-like q-Construct for a packet received by an iGate.
     * <p>
     * In packet path, this callsign must be followed by the iGate's callsign.
     * <p>
     */
    public static final Callsign qAR = DefaultCallsign.builder().base("qAR").build();

    /*
     * Common placeholder for a non-existent callsign.
     */
    public static final Callsign N0CALL = DefaultCallsign.builder().base("N0CALL").build();

    /**
     * Callsign-like indicator of a packet that should not be gated and remain
     * RF-only.
     */
    public static final Callsign NOGATE = DefaultCallsign.builder().base("NOGATE").build();

    /**
     * Callsign-like indicator of a packet that should not be gated and remain
     * RF-only.
     */
    public static final Callsign RFONLY = DefaultCallsign.builder().base("RFONLY").build();

}
