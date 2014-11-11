package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Commons;

import java.util.List;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.OBDCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.PercentageUnit;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDUnexpectedResponseException;

/**
 * Created by michal on 09.11.14.
 */
public abstract class OBDPercentageResultCommand extends OBDReadingCommand<Float>{
    protected OBDPercentageResultCommand(String cmd) { super(cmd, new PercentageUnit()); }

    @Override
    protected Float doCalculations(List<Integer> res) {
        return res.get(2)*100.0f / 255.0f;
    }
}
