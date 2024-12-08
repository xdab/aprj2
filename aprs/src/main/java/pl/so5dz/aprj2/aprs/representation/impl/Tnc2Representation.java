package pl.so5dz.aprj2.aprs.representation.impl;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import pl.so5dz.aprj2.aprs.models.Callsign;
import pl.so5dz.aprj2.aprs.models.DefaultCallsign;
import pl.so5dz.aprj2.aprs.models.DefaultPacket;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.representation.CallsignRepresentation;
import pl.so5dz.aprj2.aprs.representation.PacketRepresentation;

/**
 * Represents a Packet as a TNC2-formatted string.
 * <p>
 * The TNC2 format is a simple text-based format used in many APRS applications.
 * </p>
 * <p>
 * Example: {@code SP1ABC>APRS,WIDE1-1:Hello, World!}
 * </p>
 */
public class Tnc2Representation implements PacketRepresentation<String>, CallsignRepresentation<String> {
    private static final Tnc2Representation INSTANCE = new Tnc2Representation();
    private static final String DIRECTION_INDICATOR = ">";
    private static final String REPEATED_INDICATOR = "*";
    private static final String PATH_SEPARATOR = ",";
    private static final String SSID_SEPARATOR = "-";
    private static final String INFO_SEPARATOR = ":";

    private Tnc2Representation() {
    }

    @NonNull
    public static Tnc2Representation getInstance() {
        return INSTANCE;
    }

    @Override
    @NonNull
    public Packet toPacket(@NonNull String repr) {
        if (repr.isBlank()) {
            throw new IllegalArgumentException("TNC2 packet representation cannot be blank");
        }
        repr = repr.strip();

        String[] infoSplit = repr.split(INFO_SEPARATOR, 2);
        String preInfo = infoSplit[0];
        String info = (infoSplit.length > 1) ? infoSplit[1] : "";

        String[] dirSplit = preInfo.split(DIRECTION_INDICATOR, 2);
        String sourceRepresentation = dirSplit[0];
        Callsign source = toCallsign(sourceRepresentation);
        String postDir = (dirSplit.length > 1) ? dirSplit[1] : "";

        String[] pathSplit = postDir.split(PATH_SEPARATOR);
        String destRepresentation = pathSplit[0];
        Callsign destination = toCallsign(destRepresentation);

        List<Callsign> path = new ArrayList<>();
        for (int i = 1; i < pathSplit.length; i++) {
            path.add(toCallsign(pathSplit[i]));
        }

        return DefaultPacket.builder()
                .source(source)
                .destination(destination)
                .path(path)
                .info(info)
                .build();
    }

    @Override
    @NonNull
    public String toRepresentation(@NonNull Packet packet) {
        StringBuilder sb = new StringBuilder();
        sb.append(toRepresentation(packet.getSource()));
        sb.append(DIRECTION_INDICATOR);
        sb.append(toRepresentation(packet.getDestination()));
        List<Callsign> path = packet.getPath();
        if (path != null && !path.isEmpty()) {
            for (Callsign callsign : path) {
                sb.append(PATH_SEPARATOR);
                sb.append(toRepresentation(callsign));
            }
        }
        sb.append(INFO_SEPARATOR);
        sb.append(packet.getInfo());
        return sb.toString();
    }

    @Override
    @NonNull
    public Callsign toCallsign(@NonNull String repr) {
        if (repr.isBlank()) {
            throw new IllegalArgumentException("TNC2 callsign representation cannot be blank");
        }
        boolean repeated = false;
        if (repr.endsWith(REPEATED_INDICATOR)) {
            repeated = true;
            repr = repr.substring(0, repr.length() - 1);
        }
        String[] baseSplit = repr.split(SSID_SEPARATOR, 2);
        String base = baseSplit[0];
        int ssid = 0;
        if (baseSplit.length > 1) {
            try {
                ssid = Integer.parseInt(baseSplit[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid SSID in TNC2 callsign representation: " + repr);
            }
        }
        return DefaultCallsign.builder()
                .base(base)
                .ssid(ssid)
                .repeated(repeated)
                .build();
    }

    @Override
    @NonNull
    public String toRepresentation(@NonNull Callsign callsign) {
        StringBuilder sb = new StringBuilder();
        sb.append(callsign.getBase());
        int ssid = callsign.getSsid();
        if (ssid > 0) {
            sb.append(SSID_SEPARATOR);
            sb.append(ssid);
        }
        if (callsign.isRepeated()) {
            sb.append(REPEATED_INDICATOR);
        }
        return sb.toString();
    }
}
