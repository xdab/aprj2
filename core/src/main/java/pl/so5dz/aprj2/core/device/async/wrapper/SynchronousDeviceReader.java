package pl.so5dz.aprj2.core.device.async.wrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.core.device.async.PacketCallback;
import pl.so5dz.aprj2.core.device.sync.SynchronousDevice;

/**
 * Implementation of a {@link Runnable} that reads packets from a
 * {@link SynchronousDevice} and forwards them to a {@link PacketCallback}.
 */
@Slf4j
@RequiredArgsConstructor
class SynchronousDeviceReader implements Runnable {
    private final SynchronousDevice synchronousDevice;
    private final PacketCallback packetCallback;
    private final Runnable closeCallback;

    @Override
    public void run() {
        log.debug("Running reader for device {}", synchronousDevice.name());

        Packet packet;
        while ((packet = synchronousDevice.readPacket()) != null) {
            packetCallback.onPacketReceived(packet);
        }

        log.debug("Reader for device {} finished", synchronousDevice.name());
        closeCallback.run();
    }
}
