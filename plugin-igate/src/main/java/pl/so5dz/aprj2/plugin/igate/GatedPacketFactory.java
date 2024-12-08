package pl.so5dz.aprj2.plugin.igate;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import pl.so5dz.aprj2.aprs.constants.Callsigns;
import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.DefaultCallsign;
import pl.so5dz.aprj2.aprs.models.DefaultPacket;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.representation.impl.Tnc2Representation;

public class GatedPacketFactory {

    // q-Construct: Gated packet from RF.
    private static final Callsign qAR = DefaultCallsign.builder().base("qAR").build();

    // Indicates that the packet is coming from a third party.
    private static final String THIRD_PARTY_INDICATOR = "}";

    /**
     * Builds a packet containing indication of RX-iGating by a given iGate.
     * 
     * @param packet        Original packet
     * @param iGateCallsign iGate callsign
     * @return Packet to gate
     */
    public static Packet buildRXIGatedPacket(
            @NonNull Packet packet,
            @NonNull Callsign iGateCallsign) {
        Callsign source = DefaultCallsign.builder()
                .base(packet.getSource().getBase())
                .ssid(packet.getSource().getSsid())
                .build();
        Callsign destination = DefaultCallsign.builder()
                .base(packet.getDestination().getBase())
                .ssid(packet.getDestination().getSsid())
                .build();
        List<Callsign> path = new ArrayList<>(packet.getPath());
        path.add(qAR);
        path.add(iGateCallsign);

        return DefaultPacket.builder()
                .source(source)
                .destination(destination)
                .control(packet.getControl())
                .protocol(packet.getProtocol())
                .path(path)
                .info(packet.getInfo())
                .build();
    }

    /**
     * Builds a "third-party" packet containing the original packet
     * plus gating information.
     * 
     * @param packet        Original packet
     * @param iGateCallsign iGate callsign
     * @return Packet to gate
     */
    public static Packet buildTXIGatedPacket(
            @NonNull Packet packet,
            @NonNull Callsign iGateCallsign) {

        // Strip packet of the original path, instead
        // marking the packet as repeated by TCP/IP.
        // The path is removed to save space and thus channel utilization.
        Packet simplifiedPacket = DefaultPacket.builder()
                .source(packet.getSource())
                .destination(packet.getDestination())
                .path(List.of(Callsigns.TCPIP))
                .info(packet.getInfo())
                .build();
        String simplifiedPacketString = Tnc2Representation.getInstance()
                .toRepresentation(simplifiedPacket);

        return DefaultPacket.builder()
                .source(iGateCallsign)
                .destination(Callsigns.APRJ2)
                .control(packet.getControl())
                .protocol(packet.getProtocol())
                .info(THIRD_PARTY_INDICATOR + simplifiedPacketString)
                .build();
    }

}
