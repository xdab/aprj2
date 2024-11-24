package pl.so5dz.aprj2.plugin.beacon;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.auto.service.AutoService;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.representation.impl.Tnc2Representation;
import pl.so5dz.aprj2.plugin.DeviceInfo;
import pl.so5dz.aprj2.plugin.PeriodicallyTriggeredPlugin;
import pl.so5dz.aprj2.plugin.Plugin;
import pl.so5dz.aprj2.plugin.beacon.config.BeaconConfig;
import pl.so5dz.aprj2.plugin.beacon.config.BeaconPluginConfig;

@Slf4j
@AutoService(Plugin.class)
public class BeaconPlugin extends PeriodicallyTriggeredPlugin {
    private static final long CHECK_INTERVAL_MS = 2500;
    private static final XmlMapper xmlMapper;
    static {
        xmlMapper = new XmlMapper();
        xmlMapper.findAndRegisterModules();
        xmlMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    private List<Beacon> beacons;

    @Override
    protected Duration period() {
        return Duration.ofMillis(CHECK_INTERVAL_MS);
    }

    @Override
    protected void onTrigger() {
        if (beacons != null) {
            beacons.forEach(Beacon::trigger);
        }
    }

    @Override
    public String id() {
        return "aprj2-beacon-plugin";
    }

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public String description() {
        return "Default beacon plugin bundled with APRJ2";
    }

    @Override
    public void initialize(List<DeviceInfo> devices, Map<String, Object> parameters) {
        BeaconPluginConfig config = xmlMapper.convertValue(parameters, BeaconPluginConfig.class);
        beacons = config.getBeacons()
                .stream()
                .map(BeaconPlugin::toBeacon)
                .toList();
    }

    private static final Beacon toBeacon(BeaconConfig beaconConfig) {
        Packet packet = Tnc2Representation.getInstance().toPacket(beaconConfig.getPacket());
        return new Beacon(beaconConfig.getTarget(), packet,
                beaconConfig.getInterval(), beaconConfig.isAprsis());
    }
}