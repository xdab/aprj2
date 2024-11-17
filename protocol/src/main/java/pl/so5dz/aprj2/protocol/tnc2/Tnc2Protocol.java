package pl.so5dz.aprj2.protocol.tnc2;

import java.io.InputStream;
import java.io.OutputStream;

import pl.so5dz.aprj2.protocol.PacketInputStream;
import pl.so5dz.aprj2.protocol.PacketOutputStream;
import pl.so5dz.aprj2.protocol.Protocol;

public class Tnc2Protocol implements Protocol {
    private static final Tnc2Protocol INSTANCE = new Tnc2Protocol();

    public static Tnc2Protocol getInstance() {
        return INSTANCE;
    }

    private Tnc2Protocol() {
    }

    @Override
    public PacketInputStream createPacketInputStream(InputStream inputStream) {
        return new Tnc2InputStream(inputStream);
    }

    @Override
    public PacketOutputStream createPacketOutputStream(OutputStream outputStream) {
        return new Tnc2OutputStream(outputStream);
    }
}
