package pl.so5dz.aprj2.plugin.beacon.config;

import java.time.Duration;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BeaconConfig {
    private String target = "error";
    private String packet;
    private Duration interval;
    private boolean aprsis;
}
