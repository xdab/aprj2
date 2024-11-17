package pl.so5dz.aprj2.core.device.sync;

import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.core.device.Device;

/**
 * Represents a {@link Device} that can be read from synchronously (can receive
 * packets).
 */
public interface SynchronousDevice extends Device {

    /**
     * Reads a single packet from the device.
     *
     * @return the packet.
     */
    Packet readPacket();

}
