package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Proto;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.OBDCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDUnexpectedResponseException;

/**
 * Created by michal on 09.11.14.
 */
public class OBDAutoProtocolCommand extends OBDCommand{
    public OBDAutoProtocolCommand() {
        super("AT SP 0");
    }

    @Override
    protected boolean read(byte[] in) throws OBDUnexpectedResponseException {
        return true;
    }

    @Override
    public String name() {
        return "Choose auto protocol";
    }
}
