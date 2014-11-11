package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Temperature;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Commons.OBDTemperatureResultCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.CelsiusUnit;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.OBDUnit;

/**
 * Created by michal on 09.11.14.
 */
public class OBDEngineCoolantTemperature extends OBDTemperatureResultCommand {
    public OBDEngineCoolantTemperature() { super("01 05", new CelsiusUnit()); }

    public OBDEngineCoolantTemperature(OBDUnit unit) {
        super("01 05", unit);
    }

    @Override
    public String name() {
        return "Engine coolant temperature";
    }
}
