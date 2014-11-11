package pl.wroc.uni.ii.kokodzambocar.OBD.Loggers;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.OBDCommand;

/**
 * Created by michal on 10.11.14.
 */
public interface OBDLogger {
    public void log(OBDCommand command);
}
