package pl.so5dz.aprj2.plugin.digipeater.multipeater;

import lombok.Value;
import pl.so5dz.aprj2.aprs.packet.Packet;

@Value
public class MultipeatedPacket {
    Packet packet;
    String targetDeviceName;
}
