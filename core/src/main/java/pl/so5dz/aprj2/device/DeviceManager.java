package pl.so5dz.aprj2.device;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.core.device.async.AsynchronousDevice;

@Slf4j
public class DeviceManager {
    private final Map<String, AsynchronousDevice> devices;
    private Consumer<AsynchronousDevice> closeCallback = device -> {
    };

    public DeviceManager(List<AsynchronousDevice> devices) {
        this.devices = devices.stream()
                .collect(Collectors.toMap(AsynchronousDevice::name, Function.identity()));
        this.devices.values().forEach(device -> device.setCloseCallback(() -> this.closeCallback.accept(device)));
    }

    public void open() {
        log.debug("Opening");
        devices.values().forEach(AsynchronousDevice::open);
        log.debug("Opened");
    }

    public void close() {
        log.debug("Closing");
        devices.values().forEach(AsynchronousDevice::close);
        log.debug("Closed");
    }

    public AsynchronousDevice getDevice(String deviceName) {
        return devices.get(deviceName);
    }

    public void writePacket(String deviceName, Packet packet) {
        AsynchronousDevice device = getDevice(deviceName);
        if (device != null) {
            device.writePacket(packet);
        }
    }

    public void setPacketCallback(DevicePacketCallback packetCallback) {
        devices.values()
                .forEach(device -> device.setPacketCallback(packet -> packetCallback.onPacketReceived(device, packet)));
    }

    public void setCloseCallback(Consumer<AsynchronousDevice> closeCallback) {
        this.closeCallback = closeCallback;
    }
}
