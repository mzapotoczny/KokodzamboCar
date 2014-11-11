package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Commons.OBDPercentageResultCommand;

/**
 * Created by michal on 09.11.14.
 */
public class OBDEngineLoadCommand extends OBDPercentageResultCommand {
    public OBDEngineLoadCommand() {
        super("01 04");
    }

    @Override
    public String name() {
        return "Engine load";
    }
}
