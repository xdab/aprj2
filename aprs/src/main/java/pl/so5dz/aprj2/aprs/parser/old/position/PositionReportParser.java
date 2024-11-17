package pl.so5dz.aprj2.aprs.parser.old.position;

import pl.so5dz.aprj2.aprs.parser.models.PacketContent;
import pl.so5dz.aprj2.aprs.parser.models.Symbol;

public class PositionReportParser {
    private static final int POSITION_START = 1;
    private static final int POSITION_END = 19;

    private static final int POSITION_SYMBOL_TABLE_INDEX = 9;
    private static final int POSITION_SYMBOL_CODE_INDEX = 19;
    private static final int POSITION_COMMENT_START = 15;

    private static final int COMPRESSED_POSITION_SYMBOL_TABLE_INDEX = 1;
    private static final int COMPRESSED_POSITION_SYMBOL_CODE_INDEX = 10;
    private static final int COMPRESSED_POSITION_COMMENT_START = 20;

    public static void parsePositionReportWithoutTimestamp(PacketContent packetContent, String data) {
        var positionField = data.substring(POSITION_START, POSITION_END);
        packetContent.setPosition(PositionFieldParser.parsePosition(positionField));

        if (packetContent.getPosition().isCompressed()) {
            packetContent.setSymbol(new Symbol(data.charAt(COMPRESSED_POSITION_SYMBOL_TABLE_INDEX),
                    data.charAt(COMPRESSED_POSITION_SYMBOL_CODE_INDEX)));
            packetContent.setComment(data.substring(POSITION_COMMENT_START));
        } else {
            packetContent.setSymbol(
                    new Symbol(data.charAt(POSITION_SYMBOL_TABLE_INDEX), data.charAt(POSITION_SYMBOL_CODE_INDEX)));
            packetContent.setComment(data.substring(COMPRESSED_POSITION_COMMENT_START));
        }
    }

    private static final int TIMESTAMP_END = 8;

    private static final int POSITION_WITH_TIMESTAMP_END = 26;
    private static final int POSITION_WITH_TIMESTAMP_SYMBOL_TABLE_INDEX = 16;
    private static final int POSITION_WITH_TIMESTAMP_SYMBOL_CODE_INDEX = 26;
    private static final int POSITION_WITH_TIMESTAMP_COMMENT_START = 22;

    private static final int COMPRESSED_POSITION_WITH_TIMESTAMP_SYMBOL_TABLE_INDEX = 8;
    private static final int COMPRESSED_POSITION_WITH_TIMESTAMP_SYMBOL_CODE_INDEX = 17;
    private static final int COMPRESSED_POSITION_WITH_TIMESTAMP_COMMENT_START = 27;

    public static void parsePositionReportWithTimestamp(PacketContent packetContent, String data) {
        var positionField = data.substring(TIMESTAMP_END, POSITION_WITH_TIMESTAMP_END);
        packetContent.setPosition(PositionFieldParser.parsePosition(positionField));

        if (packetContent.getPosition().isCompressed()) {
            packetContent.setSymbol(new Symbol(data.charAt(COMPRESSED_POSITION_WITH_TIMESTAMP_SYMBOL_TABLE_INDEX),
                    data.charAt(COMPRESSED_POSITION_WITH_TIMESTAMP_SYMBOL_CODE_INDEX)));
            packetContent.setComment(data.substring(POSITION_WITH_TIMESTAMP_COMMENT_START));
        } else {
            packetContent.setSymbol(new Symbol(data.charAt(POSITION_WITH_TIMESTAMP_SYMBOL_TABLE_INDEX),
                    data.charAt(POSITION_WITH_TIMESTAMP_SYMBOL_CODE_INDEX)));
            packetContent.setComment(data.substring(COMPRESSED_POSITION_WITH_TIMESTAMP_COMMENT_START));
        }
    }
}
