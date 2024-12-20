package pl.so5dz.aprj2.aprs.parser.wip;

import java.util.ArrayList;
import java.util.List;

import pl.so5dz.aprj2.aprs.antlr.PacketBaseListener;
import pl.so5dz.aprj2.aprs.antlr.PacketParser;
import pl.so5dz.aprj2.aprs.callsign.Callsign;
import pl.so5dz.aprj2.aprs.callsign.impl.DefaultCallsign;
import pl.so5dz.aprj2.aprs.packet.Packet;
import pl.so5dz.aprj2.aprs.packet.impl.DefaultPacket;

public class PacketFactory extends PacketBaseListener {
    private DefaultPacket.DefaultPacketBuilder packetBuilder;
    private Packet packet;

    private DefaultCallsign.DefaultCallsignBuilder callsignBuilder;
    private Callsign callsign;

    private List<Callsign> path;

    public Packet getPacket() {
        return packet;
    }

    @Override
    public void enterPacket(PacketParser.PacketContext ctx) {
        packet = null;
        packetBuilder = DefaultPacket.builder();
    }

    @Override
    public void enterCallsign(PacketParser.CallsignContext ctx) {
        callsign = null;
        callsignBuilder = DefaultCallsign.builder();
    }

    @Override
    public void enterPath(PacketParser.PathContext ctx) {
        path = new ArrayList<>();
    }

    @Override
    public void exitCallsign(PacketParser.CallsignContext ctx) {
        callsign = callsignBuilder.build();
        if (path != null) {
            path.add(callsign);
        }
    }

    @Override
    public void exitCallsignBase(PacketParser.CallsignBaseContext ctx) {
        callsignBuilder.base(ctx.getText());
    }

    @Override
    public void exitSsid(PacketParser.SsidContext ctx) {
        callsignBuilder.ssid(Integer.parseInt(ctx.getText()));
    }

    @Override
    public void exitRepeated(PacketParser.RepeatedContext ctx) {
        callsignBuilder.repeated(true);
    }

    @Override
    public void exitSource(PacketParser.SourceContext ctx) {
        packetBuilder.source(callsignBuilder.build());
    }

    @Override
    public void exitDestination(PacketParser.DestinationContext ctx) {
        packetBuilder.destination(callsignBuilder.build());
    }

    @Override
    public void exitPath(PacketParser.PathContext ctx) {
        packetBuilder.path(path);
        path = null;
    }

    @Override
    public void exitInfo(PacketParser.InfoContext ctx) {
        packetBuilder.info(ctx.getText());
    }

    @Override
    public void exitPacket(PacketParser.PacketContext ctx) {
        packet = packetBuilder.build();
    }
}