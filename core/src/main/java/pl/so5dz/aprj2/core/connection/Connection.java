package pl.so5dz.aprj2.core.connection;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a connection to a device that can be opened and closed,
 * and provides input and output streams.
 */
public interface Connection {

    /**
     * Opens the connection.
     *
     * @return true if the connection was successfully opened, false otherwise.
     */
    boolean open();

    /**
     * Closes the connection and releases any resources.
     */
    void close();

    /**
     * Returns an input stream for reading from the connection.
     *
     * @return the input stream.
     */
    InputStream getInputStream();

    /**
     * Returns an output stream for writing to the connection.
     *
     * @return the output stream.
     */
    OutputStream getOutputStream();

}
