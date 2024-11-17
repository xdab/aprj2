package pl.so5dz.aprj2.aprs.parser.models
;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Item {
    @NonNull
    private String name;
    @Builder.Default
    private boolean alive = true;
}
