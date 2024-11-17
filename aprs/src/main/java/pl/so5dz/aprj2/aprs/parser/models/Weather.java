package pl.so5dz.aprj2.aprs.parser.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Weather {
    Double windDirection;
    Double windSpeed;
    Double gust;
    Double temperature;
    Double rainLastHour;
    Double rainLast24Hours;
    Double rainSinceMidnight;
    Double humidity;
    Double pressure;
}
