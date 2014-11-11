package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Temperature;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Commons.OBDTemperatureResultCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.CelsiusUnit;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.OBDUnit;

/**
 * Created by michal on 09.11.14.
 */
public class OBDAmbientAirTemperature extends OBDTemperatureResultCommand {
    public OBDAmbientAirTemperature() { super("01 46", new CelsiusUnit()); }

    public OBDAmbientAirTemperature(OBDUnit unit) {
        super("01 46", unit);
    }

    @Override
    public String name() {
        return "Air intake temperature";
    }
}
