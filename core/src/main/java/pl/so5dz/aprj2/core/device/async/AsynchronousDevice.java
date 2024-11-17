package pl.so5dz.aprj2.core.device.async;

import pl.so5dz.aprj2.core.device.Device;

/**
 * Represents a {@link Device} that can be read from asynchronously
 * using callbacks.
 */
public interface AsynchronousDevice extends Device {

    /**
     * Sets a callback for handling received packets.
     */
    void setPacketCallback(PacketCallback callback);

    /**
     * Sets a callback for handling the device being closed.
     */
    void setCloseCallback(Runnable callback);

}
