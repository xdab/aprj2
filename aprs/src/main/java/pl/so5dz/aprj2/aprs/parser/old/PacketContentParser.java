package pl.so5dz.aprj2.aprs.parser.old;

import static java.util.Objects.nonNull;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;
import pl.so5dz.aprj2.aprs.models.Packet;
import pl.so5dz.aprj2.aprs.parser.models.PacketContent;
import pl.so5dz.aprj2.aprs.parser.old.position.MicEParser;
import pl.so5dz.aprj2.aprs.parser.old.position.PositionReportParser;

@Slf4j
public class PacketContentParser {
    public static PacketContent parse(Packet packet) {
        var packetContent = new PacketContent();
        var info = packet.getInfo();
        if (StringUtils.isEmpty(info)) {
            log.warn("Packet with no data {}", packet);
            return packetContent;
        }
        info = info.stripTrailing();

        try {
            char dataType = info.charAt(0);
            switch (dataType) {
                case '!', '=' -> PositionReportParser.parsePositionReportWithoutTimestamp(packetContent, info);
                case '/', '@' -> PositionReportParser.parsePositionReportWithTimestamp(packetContent, info);
                case '`', '\'', '\u001c', '\u001d' ->
                    MicEParser.parseMicE(packetContent, info, packet.getDestination());

                default -> packetContent.setComment(info);
            }

            if (nonNull(packetContent.getComment())) {
                packetContent.setComment(packetContent.getComment());
            }

            return packetContent;

        } catch (Exception e) {
            log.error("Error while parsing packet {}", packet, e);
            return packetContent;
        }
    }
}
