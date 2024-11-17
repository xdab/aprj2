package pl.so5dz.aprj2;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.representation.impl.Tnc2Representation;
import pl.so5dz.aprj2.config.Aprj2Config;
import pl.so5dz.aprj2.config.DeviceConfig;
import pl.so5dz.aprj2.config.ProtocolConfig;
import pl.so5dz.aprj2.config.SerialConfig;
import pl.so5dz.aprj2.config.TcpConfig;
import pl.so5dz.aprj2.core.connection.Connection;
import pl.so5dz.aprj2.core.connection.serial.SerialConnection;
import pl.so5dz.aprj2.core.connection.tcp.TcpConnection;
import pl.so5dz.aprj2.core.deduplicator.Deduplicator;
import pl.so5dz.aprj2.core.device.Device;
import pl.so5dz.aprj2.core.device.async.AsynchronousDevice;
import pl.so5dz.aprj2.core.device.async.wrapper.AsynchronousDeviceWrapper;
import pl.so5dz.aprj2.core.device.sync.impl.SynchronousDeviceImpl;
import pl.so5dz.aprj2.device.DeviceManager;
import pl.so5dz.aprj2.plugin.DeviceInfo;
import pl.so5dz.aprj2.plugin.PluginManager;
import pl.so5dz.aprj2.protocol.Protocol;
import pl.so5dz.aprj2.protocol.kiss.KissProtocol;
import pl.so5dz.aprj2.protocol.tnc2.Tnc2Protocol;
import pl.so5dz.aprj2.pubsub.api.PubSub;
import pl.so5dz.aprj2.pubsub.api.Subscription;
import pl.so5dz.aprj2.pubsub.api.Topic;
import pl.so5dz.aprj2.pubsub.impl.Topics;
import pl.so5dz.aprj2.pubsub.impl.items.RxItem;
import pl.so5dz.aprj2.pubsub.impl.items.TxItem;

@Slf4j
public class Aprj2 implements Runnable {
    private final DeviceManager deviceManager;
    private final PluginManager pluginManager;
    private final Topic<RxItem> rxTopic;
    private final Topic<TxItem> txTopic;
    private final Subscription<TxItem> txSubscription;
    private final Map<String, Deduplicator> rxDeduplicators;
    private final Map<String, Deduplicator> txDeduplicators;

    private boolean running;

    public Aprj2(Aprj2Config config) {
        deviceManager = new DeviceManager(config.getDevices().stream()
                .map(Aprj2::createDevice)
                .toList());
        deviceManager.setPacketCallback(this::onPacketReceived);
        deviceManager.setCloseCallback(this::onDeviceClosed);
        rxTopic = PubSub.topic(Topics.RX, RxItem.class);
        txTopic = PubSub.topic(Topics.TX, TxItem.class);
        txSubscription = txTopic.subscribe();
        rxDeduplicators = new HashMap<>(config.getDevices().size());
        txDeduplicators = new HashMap<>(config.getDevices().size());
        pluginManager = new PluginManager(config.getPlugins());
        pluginManager.setDeviceInfo(config.getDevices().stream()
                .map(Aprj2::createDeviceInfo)
                .toList());
        pluginManager.setStopCallback(this::onPluginsStop);
    }

    @Override
    public void run() {
        if (running) {
            return;
        }
        log.debug("Running Aprj2");
        running = true;
        pluginManager.load();
        pluginManager.start();
        deviceManager.open();
        serveTxTopic();
        log.debug("Aprj2 finished");
        stop();
    }

    public void stop() {
        if (!running) {
            return;
        }
        log.debug("Stopping Aprj2");
        running = false;
        deviceManager.close();
        pluginManager.stop();
        PubSub.cancelAll();
        log.debug("Aprj2 stopped");
    }

    private void serveTxTopic() {
        TxItem txItem;
        while (running && (txItem = txSubscription.awaitMessage()) != null) {
            AsynchronousDevice targetDevice = deviceManager.getDevice(txItem.targetDeviceName());
            if (targetDevice != null) {
                onPacketTransmission(targetDevice, txItem.packet());
            }
        }
    }

    private void onPacketReceived(Device sourceDevice, Packet packet) {
        String sourceDeviceName = sourceDevice.name();
        String packetString = Tnc2Representation.getInstance().toRepresentation(packet);

        Deduplicator deduplicator = rxDeduplicators
                .computeIfAbsent(sourceDeviceName, name -> new Deduplicator(15000));

        packet = deduplicator.deduplicate(packet);
        if (packet == null) {
            log.debug("RXDUP {} {}", sourceDeviceName, packetString);
            return;
        }

        log.info("RX {} {}", sourceDeviceName, packetString);
        RxItem rxItem = new RxItem(sourceDeviceName, packet);
        rxTopic.publish(rxItem);
    }

    private void onPacketTransmission(Device targetDevice, Packet packet) {
        String targetDeviceName = targetDevice.name();
        String packetString = Tnc2Representation.getInstance().toRepresentation(packet);

        Deduplicator deduplicator = txDeduplicators
                .computeIfAbsent(targetDeviceName, name -> new Deduplicator(30000));

        packet = deduplicator.deduplicate(packet);
        if (packet == null) {
            log.debug("TXDUP {} {}", targetDeviceName, packetString);
            return;
        }

        log.info("TX {} {}", targetDeviceName, packetString);
        targetDevice.writePacket(packet);
    }

    private void onDeviceClosed(AsynchronousDevice device) {
        log.info("Device {} closed", device.name());
        stop();
    }

    private void onPluginsStop() {
        log.debug("Plugin manager stopped");
        stop();
    }

    // Factory methods

    private static AsynchronousDevice createDevice(DeviceConfig deviceConfig) {
        Protocol protocol = null;
        if (deviceConfig.getProtocol() == ProtocolConfig.KISS) {
            protocol = KissProtocol.getInstance();
        } else if (deviceConfig.getProtocol() == ProtocolConfig.TNC2) {
            protocol = Tnc2Protocol.getInstance();
        } else {
            log.error("Unsupported protocol: {}", deviceConfig.getProtocol());
            return null;
        }

        Connection connection = null;
        if (deviceConfig.isTcp()) {
            TcpConfig tcpConfig = deviceConfig.getTcp();
            connection = new TcpConnection(tcpConfig.getHost(), tcpConfig.getPort());
        } else if (deviceConfig.isSerial()) {
            SerialConfig serialConfig = deviceConfig.getSerial();
            connection = new SerialConnection(serialConfig.getPort(), serialConfig.getBaud());
        } else {
            throw new IllegalStateException("Unsupported connection type");
        }

        var synchronousDevice = new SynchronousDeviceImpl(
                deviceConfig.getName(), connection, protocol,
                deviceConfig.isTx(), deviceConfig.isRx(), deviceConfig.getInitCommand());
        return new AsynchronousDeviceWrapper(synchronousDevice);
    }

    private static DeviceInfo createDeviceInfo(DeviceConfig deviceConfig) {
        return new DeviceInfo(deviceConfig.getName(), deviceConfig.isRx(), deviceConfig.isTx());
    }
}
