package pl.so5dz.aprj2.protocol;

import java.io.InputStream;
import java.io.OutputStream;

public interface Protocol {

    PacketInputStream createPacketInputStream(InputStream inputStream);

    PacketOutputStream createPacketOutputStream(OutputStream outputStream);

}
