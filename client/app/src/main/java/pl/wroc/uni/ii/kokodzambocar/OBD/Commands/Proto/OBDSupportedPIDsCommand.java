package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Proto;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.OBDCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDUnexpectedResponseException;

/**
 * Created by michal on 09.11.14.
 */
public class OBDSupportedPIDsCommand extends OBDCommand {
    public OBDSupportedPIDsCommand() {
        super("01 00");
    }

    @Override
    protected boolean read(byte[] in) throws OBDUnexpectedResponseException {
        return true;
    }

    @Override
    public String name() {
        return "Supported PIDs";
    }
}
