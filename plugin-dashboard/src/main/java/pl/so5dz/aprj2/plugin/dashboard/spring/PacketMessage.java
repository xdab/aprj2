package pl.so5dz.aprj2.plugin.dashboard.spring;

public record PacketMessage(
        String direction,
        String device,
        String packetString) {

}
