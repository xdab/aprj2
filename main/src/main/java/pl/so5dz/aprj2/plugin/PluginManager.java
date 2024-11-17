package pl.so5dz.aprj2.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.config.PluginConfig;

@Slf4j
public class PluginManager {
    private final Map<String, Map<String, Object>> pluginConfigMap;

    private List<DeviceInfo> deviceInfo;
    private List<Plugin> plugins;
    private List<Thread> pluginThreads;
    private Runnable stopCallback;

    public PluginManager(List<PluginConfig> pluginConfigs) {
        pluginConfigMap = pluginConfigs.stream()
                .collect(Collectors.toMap(PluginConfig::getId, PluginConfig::getParams));
    }

    public void load() {
        plugins = new ArrayList<>();
        ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);
        for (Plugin plugin : loader) {
            log.info("Found plugin {}:{} ({})", plugin.id(), plugin.version(), plugin.description());
            var pluginConfig = pluginConfigMap.get(plugin.id());
            if (pluginConfig != null) {
                log.debug("Initializing plugin {} with config {}", plugin.id(), pluginConfig);
                plugin.initialize(deviceInfo, pluginConfig);
            }
            plugins.add(plugin);
        }
    }

    public void start() {
        if (plugins == null) {
            throw new IllegalStateException("Plugins not loaded");
        }
        pluginThreads = new ArrayList<>();
        plugins.forEach(plugin -> {
            Thread pluginThread = new Thread(plugin);
            pluginThread.setName(plugin.id());
            pluginThread.setUncaughtExceptionHandler(this::onPluginException);
            pluginThread.start();
            pluginThreads.add(pluginThread);
        });
    }

    public void stop() {
        if (pluginThreads == null) {
            return;
        }
        pluginThreads.forEach(Thread::interrupt);
        pluginThreads = null;
        if (stopCallback != null) {
            stopCallback.run();
        }
    }

    public void setDeviceInfo(List<DeviceInfo> deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public void setStopCallback(Runnable stopCallback) {
        this.stopCallback = stopCallback;
    }

    private void onPluginException(Thread thread, Throwable throwable) {
        log.error("Plugin {} threw an exception", thread.getName(), throwable);
        stop();
    }
}
