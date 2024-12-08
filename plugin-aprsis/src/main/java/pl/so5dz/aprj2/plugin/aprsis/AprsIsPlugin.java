package pl.so5dz.aprj2.plugin.aprsis;

import java.util.List;
import java.util.Map;

import com.google.auto.service.AutoService;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.packet.Packet;
import pl.so5dz.aprj2.aprs.representation.impl.Tnc2Representation;
import pl.so5dz.aprj2.core.device.async.AsynchronousDevice;
import pl.so5dz.aprj2.core.device.async.wrapper.AsynchronousDeviceWrapper;
import pl.so5dz.aprj2.plugin.DeviceInfo;
import pl.so5dz.aprj2.plugin.Plugin;
import pl.so5dz.aprj2.pubsub.api.PubSub;
import pl.so5dz.aprj2.pubsub.api.Subscription;
import pl.so5dz.aprj2.pubsub.api.Topic;
import pl.so5dz.aprj2.pubsub.impl.Topics;
import pl.so5dz.aprj2.pubsub.impl.items.RxItem;
import pl.so5dz.aprj2.pubsub.impl.items.TxItem;

@Slf4j
@AutoService(Plugin.class)
public class AprsIsPlugin implements Plugin {
    private static final Subscription<TxItem> txSubscription = PubSub.topic(
            Topics.TX, TxItem.class).subscribe();
    private static final Topic<RxItem> rxTopic = PubSub.topic(
            Topics.RX, RxItem.class);

    private AprsIsDevice aprsIsDevice;
    private AsynchronousDevice asynchronousDevice;

    @Override
    public void run() {
        if (aprsIsDevice == null) {
            throw new IllegalStateException("Not initialized");
        }
        log.debug("Starting");

        asynchronousDevice = new AsynchronousDeviceWrapper(aprsIsDevice);
        asynchronousDevice.setPacketCallback(this::onPacketReceived);
        asynchronousDevice.setCloseCallback(this::stop);
        asynchronousDevice.open();

        TxItem txItem;
        while ((txItem = txSubscription.awaitMessage()) != null) {
            if (txItem.targetDeviceName().equals(aprsIsDevice.name())) {
                Packet packet = txItem.packet();
                String packetString = Tnc2Representation.getInstance().toRepresentation(packet);
                log.debug("TXIS {}", packetString);
                aprsIsDevice.writePacket(packet);
            }
        }
        log.debug("Finishing");
    }

    @Override
    public void stop() {
        txSubscription.cancel();
        if (asynchronousDevice != null) {
            asynchronousDevice.close();
        }
        if (aprsIsDevice != null) {
            aprsIsDevice.close();
        }
    }

    @Override
    public String id() {
        return "aprj2-aprsis-plugin";
    }

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public String description() {
        return "Default APRS-IS plugin bundled with APRJ2";
    }

    @Override
    public void initialize(List<DeviceInfo> devices, Map<String, Object> parameters) {
        // TODO validation
        AprsIsConfig aprsIsConfig = AprsIsConfig.fromParameters(parameters);
        log.debug("Resolved {}", aprsIsConfig);
        aprsIsDevice = new AprsIsDevice(aprsIsConfig);
    }

    private void onPacketReceived(Packet packet) {
        String packetString = Tnc2Representation.getInstance().toRepresentation(packet);
        log.debug("RXIS {}", packetString);
        RxItem rxItem = new RxItem(aprsIsDevice.name(), packet);
        rxTopic.publish(rxItem);
    }
}