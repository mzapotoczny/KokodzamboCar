package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units;

/**
 * Created by michal on 09.11.14.
 */
public class CelsiusUnit extends TemperatureUnit {
    @Override
    public Float getValue() {
        return value; // default is celsius
    }

    @Override
    public String getFormattedValue() {
        if (value != null)
            return value.toString()+" *C";
        else
            return "- *C";
    }
}
