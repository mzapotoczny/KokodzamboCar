package pl.wroc.uni.ii.kokodzambocar.OBD.Commands;

import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDException;

/**
 * Created by michal on 09.11.14.
 */
public interface OBDCompletionHandler {
    public void before(OBDCommand command);
    public boolean error(OBDCommand command, OBDException exception);
    public void done(OBDCommand command);
}
