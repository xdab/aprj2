package pl.so5dz.aprj2.aprs.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RepeatingScheme extends Callsign {

    private String alias;
    private int totalHops;
    private int remainingHops;

    public RepeatingScheme(Callsign callsign) {
        super(callsign);
        String[] parts = callsign.getBase().split("[0-9]+");
        assert parts.length == 1;
        alias = parts[0];
        parts = callsign.getBase().split("[a-zA-Z]+");
        assert parts.length == 2;
        totalHops = Integer.parseInt(parts[1]);
        remainingHops = callsign.getSsid();
    }

    @Override
    public String getBase() {
        if (totalHops > 0) {
            return alias + totalHops;
        }
        return alias;
    }

    @Override
    public int getSsid() {
        return remainingHops;
    }

    @Override
    public boolean isRepeated() {
        return remainingHops == 0;
    }

    public boolean isExhausted() {
        return isRepeated();
    }

    public RepeatingScheme decrement() {
        var copy = new RepeatingScheme();
        copy.setAlias(alias);
        copy.setTotalHops(totalHops);
        copy.setRemainingHops(Math.max(0, remainingHops - 1));
        return copy;
    }
}
