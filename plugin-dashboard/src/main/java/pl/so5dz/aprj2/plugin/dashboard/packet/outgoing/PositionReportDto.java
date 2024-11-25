package pl.so5dz.aprj2.plugin.dashboard.packet.outgoing;

import lombok.Getter;

@Getter
public class PositionReportDto extends PacketDtoBase {
    private double latitude;
    private double longitude;
    private String symbol;
    private String comment;
}
