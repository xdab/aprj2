package pl.so5dz.aprj2.plugin.dashboard.common;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CallsignDto {
    String full;
    String simple;
    String base;
    int ssid;
    boolean repeated;
}
