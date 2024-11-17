package pl.so5dz.aprj2.aprs.parser.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Query {
    String addressee;
    String code;
    String text;
    Integer number;

    public boolean isAcknowledgementExpected() {
        return number != null;
    }
}
