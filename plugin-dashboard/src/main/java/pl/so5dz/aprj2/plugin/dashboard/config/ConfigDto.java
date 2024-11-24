package pl.so5dz.aprj2.plugin.dashboard.config;

import lombok.Builder;
import lombok.Value;
import pl.so5dz.aprj2.config.Aprj2Config;

@Value
@Builder
public class ConfigDto {
    Aprj2Config config;
}
