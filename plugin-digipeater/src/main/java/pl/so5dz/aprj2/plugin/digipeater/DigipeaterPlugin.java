package pl.so5dz.aprj2.plugin.digipeater;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.auto.service.AutoService;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.representation.impl.Tnc2Representation;
import pl.so5dz.aprj2.plugin.DeviceInfo;
import pl.so5dz.aprj2.plugin.PacketRxInterceptorPlugin;
import pl.so5dz.aprj2.plugin.Plugin;
import pl.so5dz.aprj2.plugin.digipeater.config.DigipeaterConfig;
import pl.so5dz.aprj2.plugin.digipeater.config.DigipeaterPluginConfig;
import pl.so5dz.aprj2.plugin.digipeater.multipeater.MultipeatedPacket;
import pl.so5dz.aprj2.plugin.digipeater.multipeater.Multipeater;
import pl.so5dz.aprj2.plugin.digipeater.multipeater.MultipeaterEntry;
import pl.so5dz.aprj2.pubsub.api.PubSub;
import pl.so5dz.aprj2.pubsub.api.Topic;
import pl.so5dz.aprj2.pubsub.impl.Topics;
import pl.so5dz.aprj2.pubsub.impl.items.TxItem;

@Slf4j
@AutoService(Plugin.class)
public class DigipeaterPlugin extends PacketRxInterceptorPlugin {
    private static final XmlMapper xmlMapper;
    static {
        xmlMapper = new XmlMapper();
        xmlMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    private final Topic<TxItem> txTopic = PubSub.topic(Topics.TX, TxItem.class);

    private Set<String> sourceDeviceNames = Set.of();
    private Multipeater multipeater;

    @Override
    protected Set<String> interceptedDeviceNames() {
        return sourceDeviceNames;
    }

    @Override
    protected void onPacketReceived(String sourceDeviceName, Packet packet) {
        List<MultipeatedPacket> multipeatedPackets = multipeater.handle(packet, sourceDeviceName);
        if (multipeatedPackets == null) {
            return;
        }
        if (multipeatedPackets != null) {
            multipeatedPackets.forEach(multipeatedPacket -> {
                TxItem txItem = new TxItem(
                        multipeatedPacket.getTargetDeviceName(),
                        multipeatedPacket.getPacket());
                txTopic.publish(txItem);
            });
        }
    }

    @Override
    public String id() {
        return "aprj2-digipeater-plugin";
    }

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public String description() {
        return "Default digipeater plugin bundled with APRJ2";
    }

    @Override
    public void initialize(List<DeviceInfo> devices, Map<String, Object> parameters) {
        DigipeaterPluginConfig config = xmlMapper.convertValue(parameters, DigipeaterPluginConfig.class);
        sourceDeviceNames = config.getDigipeaters().stream()
                .map(DigipeaterConfig::getSources)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        multipeater = new Multipeater(config.getDigipeaters()
                .stream()
                .flatMap(DigipeaterPlugin::toMultipeaterEntry)
                .toList());
    }

    private static final Stream<MultipeaterEntry> toMultipeaterEntry(DigipeaterConfig digipeaterConfig) {
        return digipeaterConfig.getSources()
                .stream()
                .map(source -> {
                    Callsign callsign = Tnc2Representation.getInstance().toCallsign(digipeaterConfig.getCallsign());
                    Digipeater digipeater = new Digipeater(callsign,
                            digipeaterConfig.getAliases().getTraced(),
                            digipeaterConfig.getAliases().getUntraced());
                    return new MultipeaterEntry(source, digipeaterConfig.getTarget(), digipeater);
                });
    }

}