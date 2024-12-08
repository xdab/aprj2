package pl.so5dz.aprj2.core.device.sync.impl;

import java.io.InputStream;
import java.io.OutputStream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.packet.Packet;
import pl.so5dz.aprj2.core.connection.Connection;
import pl.so5dz.aprj2.core.device.sync.SynchronousDevice;
import pl.so5dz.aprj2.protocol.PacketInputStream;
import pl.so5dz.aprj2.protocol.PacketOutputStream;
import pl.so5dz.aprj2.protocol.Protocol;

/**
 * Implementation of a {@link SynchronousDevice} based on a {@link Connection}
 * and a packet transfer {@link Protocol}.
 */
@Slf4j
@RequiredArgsConstructor
public class SynchronousDeviceImpl implements SynchronousDevice {
    private final String name;
    private final Connection connection;
    private final Protocol protocol;
    private final boolean tx;
    private final boolean rx;
    private final String initCommand;

    private PacketInputStream packetInputStream;
    private PacketOutputStream packetOutputStream;

    /**
     * {@inheritDoc}
     */
    public void open() {
        log.debug("Opening device {}", name);

        boolean connectionOpened = connection.open();
        if (!connectionOpened) {
            throw new RuntimeException("Failed to open connection for device " + name);
        }

        if (initCommand != null && !initCommand.isBlank()) {
            try {
                connection.getOutputStream().write(initCommand.getBytes());
            } catch (Exception e) {
                throw new RuntimeException("Failed to send init command for device " + name, e);
            }
        }

        if (tx) {
            OutputStream connectionOutputStream = connection.getOutputStream();
            packetOutputStream = protocol.createPacketOutputStream(connectionOutputStream);
        }

        if (rx) {
            InputStream connectionInputStream = connection.getInputStream();
            packetInputStream = protocol.createPacketInputStream(connectionInputStream);
        }

        log.debug("Opened device {}", name);
    }

    /**
     * {@inheritDoc}
     */
    public void close() {
        connection.close();
    }

    /**
     * {@inheritDoc}
     */
    public String name() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public void writePacket(Packet packet) {
        if (!tx) {
            throw new UnsupportedOperationException("Device does not support writing packets");
        }

        packetOutputStream.writePacket(packet);
    }

    /**
     * {@inheritDoc}
     */
    public Packet readPacket() {
        if (!rx) {
            throw new UnsupportedOperationException("Device does not support reading packets");
        }

        return packetInputStream.readPacket();
    }
}
