package pl.so5dz.aprj2.plugin.dashboard.packet.outgoing;

import lombok.Getter;

@Getter
public class MessageDto extends PacketDtoBase {
    private String addressee;
    private String message;
    private Long number;
}
