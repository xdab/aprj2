package pl.so5dz.aprj2.aprs.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

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
@AllArgsConstructor
public class DefaultCallsign extends Callsign {

    @NonNull
    private String base;

    private int ssid;

    private boolean repeated;

    public DefaultCallsign(Callsign callsign) {
        base = callsign.getBase();
        ssid = callsign.getSsid();
        repeated = callsign.isRepeated();
    }
}
