package pl.so5dz.aprj2;

import lombok.Getter;
import lombok.Setter;
import pl.so5dz.aprj2.config.Aprj2Config;
import pl.so5dz.aprj2.device.DeviceManager;
import pl.so5dz.aprj2.plugin.PluginManager;

@Getter
@Setter
public class Aprj2Context {
    private static Aprj2Context instance;

    private Aprj2Config config;
    private DeviceManager deviceManager;
    private PluginManager pluginManager;

    private Aprj2Context() {
    }

    public static synchronized Aprj2Context getInstance() {
        if (instance == null) {
            instance = new Aprj2Context();
        }
        return instance;
    }
}
