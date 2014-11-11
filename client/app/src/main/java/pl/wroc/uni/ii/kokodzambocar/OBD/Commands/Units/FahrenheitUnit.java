package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units;

/**
 * Created by michal on 09.11.14.
 */
public class FahrenheitUnit extends TemperatureUnit {

    @Override
    public Float getValue() {
        return value * 9.0f/5.0f + 32.0f;
    }

    @Override
    public String getFormattedValue() {
        return getValue().toString() + " *F";
    }
}
