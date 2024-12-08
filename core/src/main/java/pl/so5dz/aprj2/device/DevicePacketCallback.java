package pl.so5dz.aprj2.device;

import pl.so5dz.aprj2.aprs.packet.Packet;
import pl.so5dz.aprj2.core.device.Device;

@FunctionalInterface
public interface DevicePacketCallback {

    void onPacketReceived(Device device, Packet packet);

}
