package pl.so5dz.aprj2.protocol.kiss;

import java.io.InputStream;
import java.io.OutputStream;

import lombok.NonNull;
import pl.so5dz.aprj2.protocol.PacketInputStream;
import pl.so5dz.aprj2.protocol.PacketOutputStream;
import pl.so5dz.aprj2.protocol.Protocol;

public class KissProtocol implements Protocol {
    private static final KissProtocol INSTANCE = new KissProtocol();

    public static KissProtocol getInstance() {
        return INSTANCE;
    }

    private KissProtocol() {
    }

    @Override
    public PacketInputStream createPacketInputStream(@NonNull InputStream inputStream) {
        return new KissInputStream(inputStream);
    }

    @Override
    public PacketOutputStream createPacketOutputStream(@NonNull OutputStream outputStream) {
        return new KissOutputStream(outputStream);
    }

}
