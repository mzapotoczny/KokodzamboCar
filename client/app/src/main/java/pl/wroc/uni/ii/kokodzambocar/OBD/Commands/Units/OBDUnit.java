package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units;

/**
 * Created by michal on 09.11.14.
 */
public abstract class OBDUnit<T> {
    protected T value;

    public void setValue(T val) { value = val; }
    public abstract T getValue();
    public abstract String getFormattedValue();
}
