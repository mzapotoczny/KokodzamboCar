package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units;

/**
 * Created by michal on 09.11.14.
 */
public class PercentageUnit extends OBDUnit<Float> {

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public String getFormattedValue() {
        return getValue().toString() + "%";
    }
}
