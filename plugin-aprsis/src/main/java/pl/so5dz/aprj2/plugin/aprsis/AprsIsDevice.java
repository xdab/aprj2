package pl.so5dz.aprj2.plugin.aprsis;

import static pl.so5dz.aprj2.core.utils.Utils.probability;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.packet.Packet;
import pl.so5dz.aprj2.core.connection.tcp.TcpConnection;
import pl.so5dz.aprj2.core.device.sync.SynchronousDevice;
import pl.so5dz.aprj2.protocol.PacketInputStream;
import pl.so5dz.aprj2.protocol.PacketOutputStream;
import pl.so5dz.aprj2.protocol.tnc2.Tnc2Protocol;

@Slf4j
public class AprsIsDevice implements SynchronousDevice {
    private static final Tnc2Protocol protocol = Tnc2Protocol.getInstance();

    private final TcpConnection connection;
    private final String login;
    private final String passcode;
    private final String filter;

    private OutputStream rawOutputStream;
    private PacketInputStream packetInputStream;
    private PacketOutputStream packetOutputStream;

    public AprsIsDevice(AprsIsConfig aprsIsConfig) {
        connection = new TcpConnection(aprsIsConfig.server(), aprsIsConfig.port());
        login = aprsIsConfig.login();
        passcode = aprsIsConfig.passcode();
        filter = aprsIsConfig.filter();
    }

    @Override
    public String name() {
        return "aprsis";
    }

    @Override
    public void open() {
        log.debug("Opening APRS-IS device");

        if (!connection.open()) {
            throw new RuntimeException("Failed to open connection to APRS-IS");
        }

        rawOutputStream = connection.getOutputStream();
        packetInputStream = protocol.createPacketInputStream(connection.getInputStream());
        packetOutputStream = protocol.createPacketOutputStream(rawOutputStream);

        if (!sendLogin()) {
            connection.close();
            throw new RuntimeException("Failed to send APRS-IS login credentials");
        }

        log.debug("Opened APRS-IS device");
    }

    @Override
    public void close() {
        if (connection != null) {
            connection.close();
        }
        try {
            if (packetInputStream != null) {
                packetInputStream.close();
            }
            if (packetOutputStream != null) {
                packetOutputStream.close();
            }
            if (rawOutputStream != null) {
                rawOutputStream.close();
            }
        } catch (Exception e) {
            log.error("Error while closing APRS-IS device", e);
        }
    }

    @Override
    public void writePacket(Packet packet) {
        packetOutputStream.writePacket(packet);
    }

    @Override
    public Packet readPacket() {
        Packet packet;
        while (true) {
            try {
                packet = packetInputStream.readPacket();
                if (packet != null) {
                    return packet;
                }
            } catch (IllegalArgumentException e) {
                // Non-standard packets are possible to receive from APRS-IS,
                // ignore the errors associated with them and continue reading
                log.error("Non-IO error while reading packet", e);
            }
            // Upon receiving an invalid packet, blank line or comment,
            // sometimes re-send the login credentials
            // as a keep-alive mechanism
            if (probability(0.1)) {
                sendLogin();
            }
        }
    }

    private boolean sendLogin() {
        String loginString = String.format("user %s pass %s vers aprj 2.1.0 filter %s \r\n",
                login, passcode, filter);
        byte[] loginBytes = loginString.getBytes(StandardCharsets.US_ASCII);
        try {
            rawOutputStream.write(loginBytes);
            rawOutputStream.flush();
            log.debug("APRS-IS credentials sent");
            return true;
        } catch (Exception e) {
            log.error("Error while sending APRS-IS credentials", e);
            return false;
        }
    }
}
