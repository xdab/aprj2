package pl.so5dz.aprj2.aprs.parser.old.position;

import static java.util.Objects.isNull;

import pl.so5dz.aprj2.aprs.callsign.Callsign;
import pl.so5dz.aprj2.aprs.parser.models.PacketContent;
import pl.so5dz.aprj2.aprs.parser.models.Position;
import pl.so5dz.aprj2.aprs.parser.models.Symbol;

public class MicEParser {
    public static void parseMicE(PacketContent packetContent, String data, Callsign destination) {
        var sb = new StringBuilder();

        var position = packetContent.getPosition();
        if (isNull(position)) {
            position = new Position();
        }

        var destinationBase = destination.getBase();
        char northSouthIndicator = destinationBase.charAt(3);
        char longitudeOffsetIndicator = destinationBase.charAt(4);
        char eastWestIndicator = destinationBase.charAt(5);

        for (char c : destinationBase.toCharArray()) {
            sb.append(micEDestinationDigit(c));
        }
        var latitudeField = sb.toString();
        double latitude = Double.parseDouble(latitudeField.substring(0, 2));
        latitude += Double.parseDouble(latitudeField.substring(2)) / 6000.0;

        if (northSouthIndicator <= '9') {
            latitude *= -1.0;
        }

        position.setLatitude(latitude);

        double longitudeOffset = 0.0;
        if (longitudeOffsetIndicator >= 'P') {
            longitudeOffset = 100.0;
        }

        var longitudeField = data.substring(1, 4);
        char dPlus28 = longitudeField.charAt(0);
        double longitude = dPlus28 - 28 + longitudeOffset;
        if (longitude >= 180 && longitude <= 189) {
            longitude -= 180;
        } else if (longitude >= 190 && longitude <= 199) {
            longitude -= 190;
        }

        char mPlus28 = longitudeField.charAt(1);
        double longitudeMinutes = mPlus28 - 28;
        if (longitudeMinutes >= 60) {
            longitudeMinutes -= 60;
        }

        char hPlus28 = longitudeField.charAt(2);
        longitudeMinutes += (hPlus28 - 28) / 100.0;
        longitude += longitudeMinutes / 60.0;

        if (eastWestIndicator >= 'P') {
            longitude *= -1.0;
        }

        position.setLongitude(longitude);
        // TODO speed course altitude
        packetContent.setPosition(position);
        packetContent.setSymbol(new Symbol(data.charAt(8), data.charAt(7)));
        packetContent.setComment(data.substring(9));
    }

    private static char micEDestinationDigit(char c) {
        if (Character.isDigit(c)) {
            return c;
        }
        if (c >= 'A' && c <= 'J') {
            return (char) ('0' + c - 'A');
        }
        if (c >= 'P' && c <= 'Y') {
            return (char) ('0' + c - 'P');
        }
        return '0';
    }
}
