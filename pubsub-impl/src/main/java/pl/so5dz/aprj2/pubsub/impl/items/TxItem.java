package pl.so5dz.aprj2.pubsub.impl.items;

import pl.so5dz.aprj2.aprs.packet.Packet;

public record TxItem(String targetDeviceName, Packet packet) {

}
