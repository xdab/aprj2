package pl.so5dz.aprj2.aprs.parser.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    String addressee;
    boolean acknowledgement;
    boolean rejection;
    Integer number;
    String text;

    public boolean isAcknowledgementExpected() {
        return !acknowledgement && !rejection && number != null;
    }
}
