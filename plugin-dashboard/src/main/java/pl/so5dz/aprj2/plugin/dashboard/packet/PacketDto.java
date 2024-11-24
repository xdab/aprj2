package pl.so5dz.aprj2.plugin.dashboard.packet;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Value;
import pl.so5dz.aprj2.plugin.dashboard.common.CallsignDto;

@Value
@Builder
public class PacketDto {
    ZonedDateTime timestamp;
    PacketDirection direction;
    String device;
    CallsignDto source;
    CallsignDto destination;
    List<CallsignDto> path;
    String info;
}
