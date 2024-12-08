package pl.so5dz.aprj2.core;

import lombok.experimental.UtilityClass;
import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.DefaultCallsign;

@UtilityClass
public class TestConstants {

    public Callsign aprs = DefaultCallsign.builder()
            .base("APRS")
            .build();
    public Callsign n0call = DefaultCallsign.builder()
            .base("N0CALL")
            .build();
    public Callsign n0callRepeated = DefaultCallsign.builder()
            .base("N0CALL")
            .repeated(true)
            .build();
    public Callsign sampleStation = DefaultCallsign.builder()
            .base("SP1ABC")
            .ssid(1)
            .build();
    public Callsign sampleRepeater = DefaultCallsign.builder()
            .base("SR1ABC")
            .ssid(1)
            .repeated(true)
            .build();
    public Callsign wide1Available = DefaultCallsign.builder()
            .base("WIDE1")
            .ssid(1)
            .build();
    public Callsign wide1Exhausted = DefaultCallsign.builder()
            .base("WIDE1")
            .ssid(0)
            .repeated(true)
            .build();
    public Callsign sp1Available = DefaultCallsign.builder()
            .base("SP1")
            .ssid(1)
            .build();
    public Callsign sp1Exhausted = DefaultCallsign.builder()
            .base("SP1")
            .ssid(0)
            .repeated(true)
            .build();

}
