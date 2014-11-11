package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine;

import java.util.List;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Commons.OBDReadingCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.TimeUnit;

/**
 * Created by michal on 09.11.14.
 */
public class OBDEngineRuntimeCommand extends OBDReadingCommand<Integer>{
    public OBDEngineRuntimeCommand() {
        super("01 1F", new TimeUnit());
    }

    @Override
    protected Integer doCalculations(List<Integer> res) {
        return res.get(2)*256 + res.get(3);
    }

    @Override
    public String name() {
        return "Engine runtime";
    }
}
