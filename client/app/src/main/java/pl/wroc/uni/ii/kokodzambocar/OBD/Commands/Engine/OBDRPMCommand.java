package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine;

import java.util.List;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Commons.OBDReadingCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.NoneUnit;

/**
 * Created by michal on 09.11.14.
 */
public class OBDRPMCommand extends OBDReadingCommand <Float> {
    public OBDRPMCommand() {
        super("01 0C", new NoneUnit<Float>());
    }

    @Override
    protected Float doCalculations(List<Integer> res) {
        return (res.get(2)*256 + res.get(3)) / 4.0f;
    }

    @Override
    public String name() {
        return "Engine RPM";
    }
}
