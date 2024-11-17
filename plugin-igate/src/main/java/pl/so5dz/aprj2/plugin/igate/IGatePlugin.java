package pl.so5dz.aprj2.plugin.igate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.auto.service.AutoService;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.constants.Callsigns;
import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.representation.impl.Tnc2Representation;
import pl.so5dz.aprj2.plugin.DeviceInfo;
import pl.so5dz.aprj2.plugin.PacketRxInterceptorPlugin;
import pl.so5dz.aprj2.plugin.Plugin;
import pl.so5dz.aprj2.pubsub.api.PubSub;
import pl.so5dz.aprj2.pubsub.api.Topic;
import pl.so5dz.aprj2.pubsub.impl.Topics;
import pl.so5dz.aprj2.pubsub.impl.items.TxItem;

@Slf4j
@AutoService(Plugin.class)
public class IGatePlugin extends PacketRxInterceptorPlugin {
    private static final String APRSIS_DEVICE_NAME = "aprsis";

    private final Topic<TxItem> txTopic = PubSub.topic(Topics.TX, TxItem.class);

    private Set<String> sourceDeviceNames = Set.of();
    private Callsign ownCallsign;

    @Override
    protected Set<String> interceptedDeviceNames() {
        return sourceDeviceNames;
    }

    @Override
    protected void onPacketReceived(String sourceDeviceName, Packet packet) {
        boolean isMarkedRFOnly = packet.getPath().stream().anyMatch(IGatePlugin::isRFOnlyMarker);
        if (isMarkedRFOnly) {
            return; // Respect RF-only packets
        }

        boolean isThirdPartyPacket = packet.getInfo().startsWith("}");
        if (isThirdPartyPacket) {
            return; // Do not gate third-party packets
        }

        Packet gatedPacket = GatedPacketFactory.buildRXIGatedPacket(packet, ownCallsign);
        TxItem txItem = new TxItem(APRSIS_DEVICE_NAME, gatedPacket);
        txTopic.publish(txItem);
    }

    @Override
    public String id() {
        return "aprj2-igate-plugin";
    }

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public String description() {
        return "Default IGate plugin bundled with APRJ2";
    }

    @Override
    public void initialize(List<DeviceInfo> devices, Map<String, Object> parameters) {
        String ownCallsignStr = (String) parameters.getOrDefault("callsign", "N0CALL");
        ownCallsign = Tnc2Representation.getInstance().toCallsign(ownCallsignStr);
        sourceDeviceNames = devices.stream().map(DeviceInfo::name).collect(Collectors.toSet());
    }

    private static boolean isRFOnlyMarker(Callsign callsign) {
        return callsign.simpleEquals(Callsigns.NOGATE)
                || callsign.simpleEquals(Callsigns.RFONLY);
    }
}