package pl.so5dz.aprj2.plugin;

import java.util.List;
import java.util.Map;

public interface Plugin extends Runnable {
    String id();

    String version();

    String description();

    void initialize(List<DeviceInfo> devices, Map<String, Object> parameters);
}