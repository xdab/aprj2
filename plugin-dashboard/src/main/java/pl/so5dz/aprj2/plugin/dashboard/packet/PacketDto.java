package pl.so5dz.aprj2.plugin.dashboard.packet;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import pl.so5dz.aprj2.plugin.dashboard.common.CallsignDto;

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
