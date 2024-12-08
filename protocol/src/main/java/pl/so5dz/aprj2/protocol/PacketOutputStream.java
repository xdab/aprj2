package pl.so5dz.aprj2.protocol;

import java.io.FilterOutputStream;
import java.io.OutputStream;

import lombok.NonNull;
import pl.so5dz.aprj2.aprs.packet.Packet;

public abstract class PacketOutputStream extends FilterOutputStream {

    public PacketOutputStream(OutputStream out) {
        super(out);
    }

    public abstract void writePacket(@NonNull Packet packet);

}
