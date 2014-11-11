package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Proto;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.OBDCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDUnexpectedResponseException;

/**
 * Created by michal on 09.11.14.
 */
public class OBDResetCommand extends OBDCommand {
    public OBDResetCommand() {
        super("AT Z");
    }

    @Override
    protected boolean read(byte[] in) throws OBDUnexpectedResponseException { return true; }

    @Override
    public String name() {
        return "Reset OBD interface";
    }
}
