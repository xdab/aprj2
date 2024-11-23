package pl.so5dz.aprj2.plugin.dashboard.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CallsignDto {
    String callsign;
    String base;
    int ssid;
    boolean repeated;
}
