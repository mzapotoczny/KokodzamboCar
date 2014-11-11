package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units;

/**
 * Created by michal on 09.11.14.
 */
public class NoneUnit<T> extends OBDUnit<T> {

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public String getFormattedValue() {
        return getValue().toString();
    }
}
