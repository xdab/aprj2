package pl.so5dz.aprj2.core.device.async;

import pl.so5dz.aprj2.aprs.packet.Packet;

@FunctionalInterface
public interface PacketCallback {

    void onPacketReceived(Packet packet);

}
