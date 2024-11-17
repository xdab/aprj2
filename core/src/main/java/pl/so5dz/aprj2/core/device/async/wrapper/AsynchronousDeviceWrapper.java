package pl.so5dz.aprj2.core.device.async.wrapper;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.core.device.async.AsynchronousDevice;
import pl.so5dz.aprj2.core.device.async.PacketCallback;
import pl.so5dz.aprj2.core.device.sync.SynchronousDevice;

/**
 * Implementation of an {@link AsynchronousDevice} that wraps a
 * {@link SynchronousDevice}
 * and reads packets asynchronously using a separate thread.
 */
@Slf4j
public class AsynchronousDeviceWrapper implements AsynchronousDevice {
    private final SynchronousDevice synchronousDevice;
    private PacketCallback packetCallback;
    private Runnable closeCallback;
    private Thread readerThread;

    public AsynchronousDeviceWrapper(SynchronousDevice synchronousDevice) {
        this.synchronousDevice = synchronousDevice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open() {
        if (readerThread != null) {
            throw new IllegalStateException("Device already open");
        }
        if (packetCallback == null) {
            throw new IllegalStateException("Packet callback not set");
        }
        if (closeCallback == null) {
            throw new IllegalStateException("Close callback not set");
        }
        
        log.debug("Opening for device {}", name());
        synchronousDevice.open();

        var reader = new SynchronousDeviceReader(synchronousDevice, packetCallback, closeCallback);
        readerThread = new Thread(reader);
        readerThread.setName("reader-" + name());
        readerThread.setUncaughtExceptionHandler((t, e) -> {
            log.error("Uncaught exception in reader thread for device {}", name(), e);
            close();
        });
        readerThread.start();

        log.debug("Opened for device {}", name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        log.debug("Closing for device {}", name());
        if (closeCallback != null) {
            var closeCallbackCopy = closeCallback;
            closeCallback = null;
            closeCallbackCopy.run();
        }
        if (readerThread != null) {
            readerThread.interrupt();
            readerThread = null;
        }
        synchronousDevice.close();
        log.debug("Closed for device {}", name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return synchronousDevice.name();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPacketCallback(PacketCallback callback) {
        this.packetCallback = callback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCloseCallback(Runnable callback) {
        this.closeCallback = callback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writePacket(Packet packet) {
        synchronousDevice.writePacket(packet);
    }
}
