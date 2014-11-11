package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine;

import java.util.List;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Commons.OBDReadingCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.MAFUnit;

/**
 * Created by michal on 09.11.14.
 */
public class OBDMAFCommand extends OBDReadingCommand<Float> {
    public OBDMAFCommand() { super("01 10", new MAFUnit()); }

    @Override
    protected Float doCalculations(List<Integer> res) {
        return (res.get(2) * 256 + res.get(3)) / 100.0f;
    }

    @Override
    public String name() {
        return "Mass air flow";
    }
}
