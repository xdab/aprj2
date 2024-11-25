package pl.so5dz.aprj2.plugin.dashboard.packet.outgoing;

import lombok.Getter;

@Getter
public class StatusDto extends PacketDtoBase {
    private String status;
}
