package pl.so5dz.aprj2.protocol;

import java.io.FilterInputStream;
import java.io.InputStream;

import pl.so5dz.aprj2.aprs.models.Packet;

public abstract class PacketInputStream extends FilterInputStream {

    protected PacketInputStream(InputStream in) {
        super(in);
    }

    public abstract Packet readPacket();

}
