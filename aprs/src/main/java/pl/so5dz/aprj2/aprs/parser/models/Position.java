package pl.so5dz.aprj2.aprs.parser.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    private double latitude;
    private double longitude;
    private Double speed;
    private Double course;
    private Double altitude;
    public boolean compressed;
}
