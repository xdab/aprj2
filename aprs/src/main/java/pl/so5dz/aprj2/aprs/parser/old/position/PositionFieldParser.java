package pl.so5dz.aprj2.aprs.parser.old.position;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.decimal4j.util.DoubleRounder;

import pl.so5dz.aprj2.aprs.parser.models.Position;

public class PositionFieldParser {
    private static final double DEGREE_MINUTES = 60;
    private static final int ROUNDING_PRECISION = 6;

    private static final int LAT_DEGREES_START = 0;
    private static final int LAT_MINUTES_START = 2;
    private static final int LAT_NS_INDICATOR_POS = 7;
    private static final char NORTH = 'N';
    private static final char SOUTH = 'S';

    private static final int LON_DEGREES_START = 0;
    private static final int LON_MINUTES_START = 3;
    private static final int LON_EW_INDICATOR_POS = 8;
    private static final char EAST = 'E';
    private static final char WEST = 'W';

    private static final int LAT_START = 0;
    private static final int LAT_END = LAT_NS_INDICATOR_POS + 1;

    private static final int LON_START = 9;
    private static final int LON_END = LON_START + LON_EW_INDICATOR_POS + 1;

    private static final Pattern UNCOMPRESSED_POSITION_PATTERN = Pattern
            .compile("^[0-9]{4}.[0-9]{2}[NS].[01][0-9]{4}.[0-9]{2}[EW]$");

    private static final int COMPRESSED_LAT_START = 1;
    private static final int COMPRESSED_LON_START = 5;
    private static final int COMPRESSED_LON_END_EXC = 9;
    private static final int COMPRESSED_POS_BASE = 91;
    private static final int COMPRESSED_POS_OFFSET = 33;

    public static Position parsePosition(String positionField) {
        if (isCompressedPosition(positionField)) {
            return parseCompressedPosition(positionField);
        }
        return parseUncompressedPosition(positionField);
    }

    public static boolean isCompressedPosition(String positionField) {
        return !UNCOMPRESSED_POSITION_PATTERN.matcher(positionField).matches();
    }

    public static Position parseCompressedPosition(String positionField) {
        var position = new Position();
        position.setLatitude(
                parseCompressedLatitude(positionField.substring(COMPRESSED_LAT_START, COMPRESSED_LON_START)));
        position.setLongitude(
                parseCompressedLongitude(positionField.substring(COMPRESSED_LON_START, COMPRESSED_LON_END_EXC)));
        // TODO course speed altitude
        position.setCompressed(true);
        return position;
    }

    public static Double parseCompressedLatitude(String latitudeField) {
        double latitude = 0.0;
        for (char c : latitudeField.toCharArray()) {
            latitude *= COMPRESSED_POS_BASE;
            latitude += c - COMPRESSED_POS_OFFSET;
        }
        latitude = 90.0 - latitude / 380926.0;
        return latitude;
    }

    public static Double parseCompressedLongitude(String longitudeField) {
        double longitude = 0.0;
        for (char c : longitudeField.toCharArray()) {
            longitude *= COMPRESSED_POS_BASE;
            longitude += c - COMPRESSED_POS_OFFSET;
        }
        longitude = longitude / 190463.0 - 180.0;
        return longitude;
    }

    public static Position parseUncompressedPosition(String positionField) {
        var position = new Position();
        position.setLatitude(parseUncompressedLatitude(positionField.substring(LAT_START, LAT_END)));
        position.setLongitude(parseUncompressedLongitude(positionField.substring(LON_START, LON_END)));
        position.setCompressed(false);
        return position;
    }

    public static Double parseUncompressedLatitude(String latitudeField) {
        latitudeField = replaceSpaces(latitudeField);
        try {
            double degrees = Double.parseDouble(latitudeField.substring(LAT_DEGREES_START, LAT_MINUTES_START));
            degrees += Double.parseDouble(latitudeField.substring(LAT_MINUTES_START, LAT_NS_INDICATOR_POS))
                    / DEGREE_MINUTES;
            char northSouthIndicator = latitudeField.charAt(LAT_NS_INDICATOR_POS);
            if (SOUTH == northSouthIndicator) {
                degrees = -degrees;
            } else if (NORTH != northSouthIndicator) {
                throw new RuntimeException("North/South indicator was " + northSouthIndicator);
            }
            return DoubleRounder.round(degrees, ROUNDING_PRECISION);
        } catch (Exception e) {
            // // log.error("Error while parsing latitude '{}'", latitudeField, e);
        }
        return null;
    }

    public static Double parseUncompressedLongitude(String longitudeField) {
        longitudeField = replaceSpaces(longitudeField);
        try {
            double degrees = Double.parseDouble(longitudeField.substring(LON_DEGREES_START, LON_MINUTES_START));
            degrees += Double.parseDouble(longitudeField.substring(LON_MINUTES_START, LON_EW_INDICATOR_POS))
                    / DEGREE_MINUTES;
            char eastWestIndicator = longitudeField.charAt(LON_EW_INDICATOR_POS);
            if (WEST == eastWestIndicator) {
                degrees = -degrees;
            } else if (EAST != eastWestIndicator) {
                throw new RuntimeException("East/West indicator was " + eastWestIndicator);
            }
            return DoubleRounder.round(degrees, ROUNDING_PRECISION);
        } catch (Exception e) {
            // // log.error("Error while parsing longitude '{}'", longitudeField, e);
        }
        return null;
    }

    private static String replaceSpaces(String str) {
        return StringUtils.replaceChars(str, ' ', '0');
    }
}
