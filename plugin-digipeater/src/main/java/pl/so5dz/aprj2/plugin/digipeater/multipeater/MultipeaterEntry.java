package pl.so5dz.aprj2.plugin.digipeater.multipeater;

import lombok.Getter;
import lombok.ToString;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.plugin.digipeater.Digipeater;

@Getter
@ToString
public class MultipeaterEntry {
    /**
     * Name of the device from which the packet was received.
     */
    private String sourceDeviceName;

    /**
     * Name of the device to which the packet should be transmitted.
     */
    private String targetDeviceName;

    /**
     * The underlying Digipeater.
     */
    private Digipeater digipeater;

    /**
     * Copies a Digipeater and sets the source and target device names.
     * 
     * @param sourceDeviceName see field {@link #sourceDeviceName}
     * @param targetDeviceName see field {@link #targetDeviceName}
     * @param digipeater       the {@link Digipeater} to wrap
     */
    public MultipeaterEntry(String sourceDeviceName, String targetDeviceName, Digipeater digipeater) {
        this.sourceDeviceName = sourceDeviceName;
        this.targetDeviceName = targetDeviceName;
        this.digipeater = digipeater;
    }

    public Packet handle(Packet packet) {
        return digipeater.handle(packet);
    }
}
