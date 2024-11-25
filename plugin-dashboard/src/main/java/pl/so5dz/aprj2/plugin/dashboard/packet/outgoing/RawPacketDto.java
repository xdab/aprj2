package pl.so5dz.aprj2.plugin.dashboard.packet.outgoing;

import lombok.Getter;

@Getter
public class RawPacketDto {
    private String device;
    private String raw;
}
