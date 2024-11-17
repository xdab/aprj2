package pl.so5dz.aprj2.aprs.parser.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacketContent {
    @NonNull
    @Builder.Default
    private PacketType type = PacketType.TEST;

    private Position position;
    private Timestamp timestamp;
    private String status;
    private Telemetry telemetry;
    private Weather weather;
    private Symbol symbol;
    private String comment;

    // Object-like
    private Item item;
    private Object object;

    // Message-like
    private Message message;
    private Bulletin bulletin;
    private Announcement announcement;

    // Query related
    private Query query;
    private Capabilities capabilities;
}
