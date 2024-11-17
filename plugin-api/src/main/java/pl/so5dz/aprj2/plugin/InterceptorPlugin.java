package pl.so5dz.aprj2.plugin;

import java.util.Set;

/**
 * Base class for plugins that "intercept" one or more devices.
 */
public abstract class InterceptorPlugin implements Plugin {

    /**
     * Names of devices that this plugin should intercept packets to/from.
     * <p>
     * If empty, nothing is intercepted. The plugin will never get to handle
     * any packets.
     * </p>
     * <p>
     * If {@code null}, everything is intercepted. The plugin will get to handle
     * packets from all available devices.
     * </p>
     */
    protected abstract Set<String> interceptedDeviceNames();

    /**
     * Checks if the given device name is intercepted by this plugin.
     *
     * @param deviceName name of the device
     * @return {@code true} if the device is intercepted, {@code false} otherwise
     */
    protected boolean isIntercepted(String deviceName) {
        return interceptedDeviceNames() == null || interceptedDeviceNames().contains(deviceName);
    }

}
