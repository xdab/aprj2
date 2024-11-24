package pl.so5dz.aprj2.plugin.dashboard.packet;

import lombok.Data;

@Data
public class RawPacketDto {
    private String device;
    private String raw;
}
