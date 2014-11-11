package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Commons;

import java.util.List;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.CelsiusUnit;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.OBDUnit;

/**
 * Created by michal on 09.11.14.
 */
public abstract class OBDTemperatureResultCommand extends OBDReadingCommand<Float> {
    public OBDTemperatureResultCommand(String command, OBDUnit unit) { super(command, unit); }

    @Override
    protected Float doCalculations(List<Integer> res) {
        return res.get(2) - 40.0f;
    }

}
