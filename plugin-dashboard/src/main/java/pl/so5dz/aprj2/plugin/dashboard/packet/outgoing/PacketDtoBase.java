package pl.so5dz.aprj2.plugin.dashboard.packet.outgoing;

import lombok.Getter;
import pl.so5dz.aprj2.aprs.constants.Callsigns;

@Getter
public abstract class PacketDtoBase {
    protected String device;
    protected String source;
    protected String destination = Callsigns.APRJ2.getBase();
}
