package pl.so5dz.aprj2.plugin.dashboard.dto;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PacketDto {
    PacketDirection direction;
    String device;
    String rawPacket;
    CallsignDto source;
    CallsignDto destination;
    List<CallsignDto> path;
}
