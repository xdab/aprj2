package pl.so5dz.aprj2.plugin.dx;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.auto.service.AutoService;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.constants.Callsigns;
import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.parser.models.PacketContent;
import pl.so5dz.aprj2.aprs.parser.models.Position;
import pl.so5dz.aprj2.aprs.parser.old.PacketContentParser;
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
public class DxPlugin extends PacketRxInterceptorPlugin {
    private static final String APRSIS_DEVICE_NAME = "aprsis";
    private final Topic<TxItem> txTopic = PubSub.topic(Topics.TX, TxItem.class);

    private Set<String> sourceDeviceNames = Set.of();
    private String targetDeviceName;
    private Callsign ownCallsign;
    private Coordinates ownCoordinates;
    private double dxDistanceThreshold = 100.0;
    private double dxDistanceMax = 1000.0;
    private boolean aprsis;

    @Override
    protected Set<String> interceptedDeviceNames() {
        return sourceDeviceNames;
    }

    @Override
    public void onPacketReceived(String sourceDeviceName, Packet packet) {
        boolean isRepeated = packet.getPath().stream()
                .anyMatch(Callsign::isRepeated);
        boolean isRepeatedWithUntracedScheme = packet.getPath().stream()
                .anyMatch(DxPlugin::isUsedUntracedScheme);
        if (isRepeated || isRepeatedWithUntracedScheme) {
            // Only interested in directly received packets
            return;
        }
        PacketContent packetContent = PacketContentParser.parse(packet);
        if (packetContent == null) {
            // Could not parse packet content - skip
            return;
        }
        Position position = packetContent.getPosition();
        if (position == null) {
            // Not a position packet - skip
            return;
        }
        Coordinates coordinates = new Coordinates(position.getLatitude(), position.getLongitude());
        double distance = ownCoordinates.distanceTo(coordinates);
        if (distance < dxDistanceThreshold) {
            // Not a DX packet - skip
            return;
        }
        if (distance > dxDistanceMax) {
            // Unreasonably large distance, probably position input error or packet
            // corruption - skip
            return;
        }
        String packetString = Tnc2Representation.getInstance().toRepresentation(packet);
        log.info("DX {}km {}", distance, packetString);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("Interrupted", e);
            Thread.currentThread().interrupt();
        }

        Packet dxStatusPacket = buildDxStatusPacket(packet, distance);
        txTopic.publish(new TxItem(targetDeviceName, dxStatusPacket));
        if (aprsis) {
            txTopic.publish(new TxItem(APRSIS_DEVICE_NAME, dxStatusPacket));
        }
    }

    @Override
    public String id() {
        return "aprj2-dx-plugin";
    }

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public String description() {
        return "APRJ2 DX Plugin";
    }

    @Override
    public void initialize(List<DeviceInfo> devices, Map<String, Object> parameters) {
        double ownLatitude = Double.parseDouble((String) parameters.get("latitude"));
        double ownLongitude = Double.parseDouble((String) parameters.get("longitude"));
        ownCoordinates = new Coordinates(ownLatitude, ownLongitude);
        if (parameters.containsKey("dxDistance")) {
            dxDistanceThreshold = Double.parseDouble((String) parameters.get("dxDistance"));
        }
        ownCallsign = Tnc2Representation.getInstance().toCallsign(
                (String) parameters.get("callsign"));
        String sourceDeviceName = (String) parameters.get("source");
        sourceDeviceNames = Set.of(sourceDeviceName);
        targetDeviceName = (String) parameters.get("target");
        aprsis = Boolean.parseBoolean((String) parameters.getOrDefault("aprsis", "false"));
    }

    private Packet buildDxStatusPacket(Packet originalPacket, double distance) {
        String stationCallsignString = Tnc2Representation.getInstance().toRepresentation(originalPacket.getSource());
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String timeString = sdf.format(now);
        return Packet.builder()
                .source(ownCallsign)
                .destination(Callsigns.APRJ2)
                .info(String.format(">DX %s %.1fkm @ %s", stationCallsignString, distance, timeString))
                .build();
    }

    private static boolean isUsedUntracedScheme(Callsign callsign) {
        String base = callsign.getBase();
        boolean isUntracedScheme = (base.length() == 3)
                && Character.isAlphabetic(base.charAt(0))
                && Character.isAlphabetic(base.charAt(1))
                && Character.isDigit(base.charAt(2));
        if (!isUntracedScheme) {
            return false;
        }
        int totalHops = Integer.parseInt(base.substring(2));
        int remainingHops = callsign.getSsid();
        return remainingHops < totalHops;
    }
}