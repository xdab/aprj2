package pl.so5dz.aprj2.aprs.parser.models;

import lombok.Value;

@Value
public class Symbol {
    Character table;
    Character code;

    @Override
    public String toString() {
        return String.valueOf(table) + code;
    }
}
