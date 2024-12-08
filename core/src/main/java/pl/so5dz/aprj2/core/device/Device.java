package pl.so5dz.aprj2.core.device;

import pl.so5dz.aprj2.aprs.packet.Packet;

/**
 * Represents a named device that can be opened, closed, and written to (can
 * transmit packets).
 */
public interface Device {

    /**
     * Opens the device.
     *
     * @return true if the device was successfully opened, false otherwise.
     */
    void open();

    /**
     * Closes the device and releases any resources.
     */
    void close();

    /**
     * Writes a single packet to the device.
     *
     * @param packet the packet to write.
     */
    void writePacket(Packet packet);

    /**
     * Returns the name of the device.
     *
     * @return the name.
     */
    String name();

}
