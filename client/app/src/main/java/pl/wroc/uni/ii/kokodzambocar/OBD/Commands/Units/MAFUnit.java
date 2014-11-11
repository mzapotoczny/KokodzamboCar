package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units;

/**
 * Created by michal on 09.11.14.
 */
public class MAFUnit extends NoneUnit<Float> {
    @Override
    public String getFormattedValue() { return String.format("%.2f%s", value, "g/s"); }
}
