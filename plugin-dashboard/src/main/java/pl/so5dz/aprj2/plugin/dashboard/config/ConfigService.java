package pl.so5dz.aprj2.plugin.dashboard.config;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.so5dz.aprj2.Aprj2Context;
import pl.so5dz.aprj2.config.Aprj2Config;

@Service
@RequiredArgsConstructor
public class ConfigService {
    private final Aprj2Context aprj2Context;

    public Aprj2Config getConfig() {
        return aprj2Context.getConfig();
    }
}
