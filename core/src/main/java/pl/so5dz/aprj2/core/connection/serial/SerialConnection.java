package pl.so5dz.aprj2.core.connection.serial;

import java.io.InputStream;
import java.io.OutputStream;

import com.fazecast.jSerialComm.SerialPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.core.connection.Connection;

/**
 * A Serial connection implemented using
 * <a href="https://fazecast.github.io/jSerialComm/">jSerialComm</a>.
 */
@Slf4j
@RequiredArgsConstructor
public class SerialConnection implements Connection {
    private final String port;
    private final int baud;

    private SerialPort serialPort;

    @Override
    public boolean open() {
        serialPort = SerialPort.getCommPort(port);
        serialPort.setBaudRate(baud);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
        if (serialPort.openPort()) {
            log.debug("Serial port '{}' opened successfully", port);
            return true;
        } else {
            log.error("Failed to open serial port '{}'", port);
            return false;
        }
    }

    @Override
    public void close() {
        if (serialPort != null) {
            serialPort.closePort();
            log.debug("Serial port '{}' closed", port);
        }
    }

    @Override
    public InputStream getInputStream() {
        return serialPort.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return serialPort.getOutputStream();
    }

}
