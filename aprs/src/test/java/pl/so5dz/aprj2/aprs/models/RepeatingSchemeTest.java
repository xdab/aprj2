package pl.so5dz.aprj2.aprs.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class RepeatingSchemeTest {

    @Test
    void testCopyConstructor() {
        Callsign callsign = Callsign.fromString("WIDE2-1");
        RepeatingScheme repeatingScheme = new RepeatingScheme(callsign);
        assertEquals("WIDE", repeatingScheme.getAlias());
        assertEquals(2, repeatingScheme.getTotalHops());
        assertEquals(1, repeatingScheme.getRemainingHops());

        assertThrows(AssertionError.class, () -> new RepeatingScheme(
                Callsign.fromString("SO5DZ")));
    }

    @Test
    void testGetBase() {
        RepeatingScheme repeatingScheme = new RepeatingScheme();
        repeatingScheme.setAlias("ALIAS");

        repeatingScheme.setTotalHops(123);
        assertEquals("ALIAS123", repeatingScheme.getBase());

        repeatingScheme.setTotalHops(0);
        assertEquals("ALIAS", repeatingScheme.getBase());
    }

    @Test
    void testGetSsid() {
        RepeatingScheme repeatingScheme = new RepeatingScheme();
        repeatingScheme.setRemainingHops(123);
        assertEquals(123, repeatingScheme.getSsid());
    }

    @Test
    void testIsRepeated() {
        RepeatingScheme repeatingScheme = new RepeatingScheme();
        repeatingScheme.setTotalHops(123);
        repeatingScheme.setRemainingHops(0);
        assertEquals(true, repeatingScheme.isRepeated());

        repeatingScheme.setRemainingHops(100);
        assertEquals(false, repeatingScheme.isRepeated());
    }
}
