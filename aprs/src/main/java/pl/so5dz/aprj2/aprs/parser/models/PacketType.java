package pl.so5dz.aprj2.aprs.parser.models
;

import java.util.HashSet;
import java.util.Set;

public enum PacketType {
    POSITION('!', '=', '/', '@'),
    MIC_E('\'', '.', (char) 0x1c, (char) 0x1d),
    OBJECT(';'),
    ITEM(')'),
    WEATHER('!', '#', '$', '*', '_'),
    TELEMETRY('T'),
    MESSAGE(':'),
    STATION_CAPABILITIES('<'),
    QUERY('?'),
    STATUS('>'),
    TEST(','),
    USER_DEFINED('{');

    private final Set<Character> identifiers;

    PacketType(char... identifiers) {
        this.identifiers = new HashSet<>(identifiers.length);
        for (char id : identifiers) {
            this.identifiers.add(id);
        }
    }

    public static PacketType fromIdentifier(char identifier) {
        for (PacketType type : values()) {
            for (char id : type.identifiers) {
                if (id == identifier) {
                    return type;
                }
            }
        }
        throw new IllegalArgumentException(
                "Unknown packet type identifier: " + identifier);
    }
}
