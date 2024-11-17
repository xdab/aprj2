package pl.so5dz.aprj2.plugin.digipeater;

import lombok.experimental.UtilityClass;
import pl.so5dz.aprj2.aprs.models.Callsign;

@UtilityClass
public class TestConstants {

    public Callsign aprs = Callsign.builder()
            .base("APRS")
            .build();
    public Callsign n0call = Callsign.builder()
            .base("N0CALL")
            .build();
    public Callsign n0callRepeated = Callsign.builder()
            .base("N0CALL")
            .repeated(true)
            .build();
    public Callsign sampleStation = Callsign.builder()
            .base("SP1ABC")
            .ssid(1)
            .build();
    public Callsign sampleRepeater = Callsign.builder()
            .base("SR1ABC")
            .ssid(1)
            .repeated(true)
            .build();
    public Callsign wide1Available = Callsign.builder()
            .base("WIDE1")
            .ssid(1)
            .build();
    public Callsign wide1Exhausted = Callsign.builder()
            .base("WIDE1")
            .ssid(0)
            .repeated(true)
            .build();
    public Callsign sp1Available = Callsign.builder()
            .base("SP1")
            .ssid(1)
            .build();
    public Callsign sp1Exhausted = Callsign.builder()
            .base("SP1")
            .ssid(0)
            .repeated(true)
            .build();

}
