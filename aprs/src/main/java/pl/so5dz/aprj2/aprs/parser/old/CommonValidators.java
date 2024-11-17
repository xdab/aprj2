package pl.so5dz.aprj2.aprs.parser.old;

public class CommonValidators {
    private static final double MINIMUM_LONGITUDE = -180.0;
    private static final double MAXIMUM_LONGITUDE = 180.0;

    private static final double MINIMUM_LATITUDE = -90.0;
    private static final double MAXIMUM_LATITUDE = 90.0;

    private static final int SYMBOL_LENGTH = 2;
    private static final int MINIMUM_PRINTABLE_ASCII = 0x21;
    private static final int MAXIMUM_PRINTABLE_ASCII = 0x7e;
    private static final char PRIMARY_SYMBOL_TABLE = '/';
    private static final char SECONDARY_SYMBOL_TABLE = '\\';

    public static void validateLongitude(double longitude) {
        if (longitude < MINIMUM_LONGITUDE || longitude > MAXIMUM_LONGITUDE) {
            throw new IllegalArgumentException("Longitude must be between " + MINIMUM_LONGITUDE + " and " + MAXIMUM_LONGITUDE + " degrees");
        }
    }

    public static void validateLatitude(double latitude) {
        if (latitude < MINIMUM_LATITUDE || latitude > MAXIMUM_LATITUDE) {
            throw new IllegalArgumentException("Latitude must be between " + MINIMUM_LATITUDE + " and " + MAXIMUM_LATITUDE + " degrees");
        }
    }

    public static void validateSymbol(String symbol) {
        if (symbol.length() != SYMBOL_LENGTH) {
            throw new IllegalArgumentException("Symbol must be " + SYMBOL_LENGTH + " characters long");
        }
        for (int i = 0; i < SYMBOL_LENGTH; i++) {
            char c = symbol.charAt(i);
            if (c < MINIMUM_PRINTABLE_ASCII || c > MAXIMUM_PRINTABLE_ASCII) {
                throw new IllegalArgumentException("Symbol must be printable ASCII");
            }
        }
        char symbolTable = symbol.charAt(0);
        if (symbolTable != PRIMARY_SYMBOL_TABLE && symbolTable != SECONDARY_SYMBOL_TABLE) {
            throw new IllegalArgumentException("Symbol table must be " + PRIMARY_SYMBOL_TABLE + " or " + SECONDARY_SYMBOL_TABLE);
        }
    }
}
