package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Commons.OBDPercentageResultCommand;

/**
 * Created by michal on 09.11.14.
 */
public class OBDThrottlePositionCommand extends OBDPercentageResultCommand {
    public OBDThrottlePositionCommand() { super("01 11"); }

    @Override
    public String name() {
        return "Throttle Position";
    }
}
