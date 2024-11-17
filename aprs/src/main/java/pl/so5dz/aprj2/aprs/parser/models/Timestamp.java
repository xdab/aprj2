package pl.so5dz.aprj2.aprs.parser.models
;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Timestamp {
    @NonNull
    private Timezone zone;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer minute;
    private Integer second;
}
