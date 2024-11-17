package pl.so5dz.aprj2.aprs.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import pl.so5dz.aprj2.aprs.constants.Callsigns;

public class CallsignTest {
    @Test
    void testIsLegalCallsign() {
        // legal callsigns

        Callsign callsign = Callsign.fromString("AB1CD");
        assertTrue(callsign.isLegalCallsign());

        callsign = Callsign.fromString("E1A-1*");
        assertTrue(callsign.isLegalCallsign());

        callsign = Callsign.fromString("SP100SPECJALNY-14");
        assertTrue(callsign.isLegalCallsign());

        // non-legal callsigns

        callsign = Callsign.fromString("HELLO");
        assertFalse(callsign.isLegalCallsign());

        callsign = Callsign.fromString("WIDE2-1");
        assertFalse(callsign.isLegalCallsign());

        callsign = Callsign.fromString("SP3-2");
        assertFalse(callsign.isLegalCallsign());

        assertFalse(Callsigns.APRJ2.isLegalCallsign());
        assertFalse(Callsigns.TCPIP.isLegalCallsign());
        assertFalse(Callsigns.NOGATE.isLegalCallsign());
        assertFalse(Callsigns.RFONLY.isLegalCallsign());
        assertFalse(Callsigns.qAR.isLegalCallsign());
    }

    @Test
    void testSimpleEquals() {
        assertTrue(Callsigns.APRJ2.simpleEquals(Callsigns.APRJ2));
        assertTrue(Callsigns.APRJ2.simpleEquals(Callsign.fromString("APRJ2*")));
        assertFalse(Callsigns.APRJ2.simpleEquals(Callsign.fromString("APRJ2-2")));
        assertFalse(Callsigns.APRJ2.simpleEquals(Callsign.fromString("LOL123")));
    }

    @Test
    void testSimpleHashCode() { // Like simpleEquals, but with hash codes
        assertEquals(Callsigns.APRJ2.simpleHashCode(), Callsigns.APRJ2.simpleHashCode());
        assertEquals(Callsigns.APRJ2.simpleHashCode(), Callsign.fromString("APRJ2*").simpleHashCode());
        assertNotEquals(Callsigns.APRJ2.simpleHashCode(), Callsign.fromString("APRJ2-2").simpleHashCode());
        assertNotEquals(Callsigns.APRJ2.simpleHashCode(), Callsign.fromString("LOL123").simpleHashCode());
    }
}
