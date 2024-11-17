package pl.so5dz.aprj2.core.connection.tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.core.connection.Connection;

/**
 * A TCP connection implemented using a {@link java.net.Socket}.
 */
@Slf4j
@RequiredArgsConstructor
public class TcpConnection implements Connection {
    private final String host;
    private final int port;

    private Socket socket;

    @Override
    public boolean open() {
        if (socket != null) {
            return true;
        }
        log.debug("Opening socket for {}:{}", host, port);
        try {
            socket = new Socket(host, port);
            return true;
        } catch (Exception e) {
            log.error("Error while opening socket for {}:{}", host, port, e);
            return false;
        }
    }

    @Override
    public void close() {
        if (socket == null) {
            return;
        }
        log.debug("Closing socket for {}:{}", host, port);
        try {
            socket.close();
        } catch (Exception e) {
            log.error("Error while closing socket", e);
        } finally {
            socket = null;
        }
    }

    @Override
    public InputStream getInputStream() {
        try {
            return socket.getInputStream();
        } catch (Exception e) {
            log.error("Error while getting input stream", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public OutputStream getOutputStream() {
        try {
            return socket.getOutputStream();
        } catch (Exception e) {
            log.error("Error while getting output stream", e);
            throw new RuntimeException(e);
        }
    }
}
