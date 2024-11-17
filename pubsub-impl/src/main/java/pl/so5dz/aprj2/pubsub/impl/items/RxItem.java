package pl.so5dz.aprj2.pubsub.impl.items;

import pl.so5dz.aprj2.aprs.models.Packet;

public record RxItem(String sourceDeviceName, Packet packet) {
}