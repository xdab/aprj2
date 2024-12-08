package pl.so5dz.aprj2.aprs.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import pl.so5dz.aprj2.aprs.constants.Callsigns;

/**
 * Represents a single AX.25 packet.
 */
@Builder
public class DefaultPacket extends Packet {

    @Builder.Default
    private Callsign source = Callsigns.N0CALL;

    @Builder.Default
    private Callsign destination = Callsigns.APRJ2;

    @Builder.Default
    private List<Callsign> path = new ArrayList<>();

    @Builder.Default
    private int control = 0x03;

    @Builder.Default
    private int protocol = 0xf0;

    private String info;

    @Override
    public Callsign getSource() {
        return source;
    }

    @Override
    public Callsign getDestination() {
        return destination;
    }

    @Override
    @SuppressWarnings("unchecked") // DefaultCallsign implements Callsign
    public List<Callsign> getPath() {
        return (List<Callsign>) (List<?>) path;
    }

    @Override
    public int getControl() {
        return control;
    }

    @Override
    public int getProtocol() {
        return protocol;
    }

    @Override
    public String getInfo() {
        return info;
    }
}