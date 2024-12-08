package pl.so5dz.aprj2.aprs.representation.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.DefaultCallsign;
import pl.so5dz.aprj2.aprs.models.DefaultPacket;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.representation.PacketRepresentation;

/**
 * Represents a Packet as AX.25 / KISS compatible byte array.
 * <p>
 * Start flags, end flags, and FCS are not included in the representation,
 * but may easily be added by the caller based on the returned byte array.
 * </p>
 */
public class Ax25Representation implements PacketRepresentation<byte[]> {
    private static final Ax25Representation INSTANCE = new Ax25Representation();

    private Ax25Representation() {
    }

    @NonNull
    public static Ax25Representation getInstance() {
        return INSTANCE;
    }

    @Override
    public Packet toPacket(@NonNull byte[] repr) {
        if (repr.length < 16) {
            throw new IllegalArgumentException("AX.25 packet representation is too short");
        }

        var destBuilder = DefaultCallsign.builder();
        readCallsign(destBuilder, repr, 0);
        Callsign destination = destBuilder.build();

        var sourceBuilder = DefaultCallsign.builder();
        boolean last = readCallsign(sourceBuilder, repr, 7);
        Callsign source = sourceBuilder.build();

        List<Callsign> path = new ArrayList<>();
        int i = 14;
        while (!last && i < repr.length - 7) {
            var callsignBuilder = DefaultCallsign.builder();
            last = readCallsign(callsignBuilder, repr, i);
            path.add(callsignBuilder.build());
            i += 7;
        }

        int control = repr[i++];
        int protocol = repr[i++];

        byte[] infoBytes = new byte[repr.length - i];
        String info = null;
        if (infoBytes.length > 0) {
            System.arraycopy(repr, i, infoBytes, 0, infoBytes.length);
            info = new String(infoBytes);
        }

        return DefaultPacket.builder()
                .source(source)
                .destination(destination)
                .path(path)
                .control(control)
                .protocol(protocol)
                .info(info)
                .build();
    }

    @Override
    @NonNull
    public byte[] toRepresentation(@NonNull Packet packet) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            writeCallsign(baos, packet.getDestination(), false);
            int pathLength = packet.getPath().size();
            writeCallsign(baos, packet.getSource(), pathLength == 0);
            for (int i = 0; i < pathLength; i++) {
                Callsign callsign = packet.getPath().get(i);
                boolean isLast = i == (pathLength - 1);
                writeCallsign(baos, callsign, isLast);
            }
            baos.write(packet.getControl());
            baos.write(packet.getProtocol());
            baos.write(packet.getInfo().getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Error while converting packet to AX.25 representation", e);
        }
        return baos.toByteArray();
    }

    private static void writeCallsign(ByteArrayOutputStream baos, Callsign callsign, boolean isLast)
            throws IOException {
        String base = callsign.getBase();
        for (int i = 0; i < 6; i++) {
            if (i < base.length()) {
                baos.write(base.charAt(i) << 1);
            } else {
                baos.write(' ' << 1);
            }
        }
        int metadataByte = 0b01100000;
        metadataByte |= callsign.isRepeated() ? 0x80 : 0x00;
        metadataByte |= assertValidSsid(callsign.getSsid()) << 1;
        metadataByte |= isLast ? 1 : 0;
        baos.write(metadataByte);
    }

    private static boolean readCallsign(
            DefaultCallsign.DefaultCallsignBuilder callsignBuilder, byte[] repr, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int charByte = repr[startIndex + i] & 0xff;
            char c = (char) (charByte >> 1);
            if (c == ' ') {
                break;
            }
            sb.append(c);
        }
        int metadataByte = repr[startIndex + 6];
        boolean repeated = (metadataByte & 0x80) != 0;
        int ssid = (metadataByte >> 1) & 0x0F;
        boolean last = (metadataByte & 0x01) != 0;
        callsignBuilder.base(sb.toString())
                .ssid(ssid)
                .repeated(repeated);
        return last;
    }

    private static int assertValidSsid(int ssid) {
        if (ssid < 0 || ssid > 15) {
            throw new IllegalArgumentException("Invalid SSID: " + ssid);
        }
        return ssid;
    }
}
