package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Temperature;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Commons.OBDTemperatureResultCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.CelsiusUnit;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.OBDUnit;

/**
 * Created by michal on 09.11.14.
 */
public class OBDAirIntakeTemperatureCommand extends OBDTemperatureResultCommand {
    public OBDAirIntakeTemperatureCommand() { super("01 0F", new CelsiusUnit()); }

    public OBDAirIntakeTemperatureCommand(OBDUnit unit) {
        super("01 0F", unit);
    }

    @Override
    public String name() {
        return "Air intake temperature";
    }
}
