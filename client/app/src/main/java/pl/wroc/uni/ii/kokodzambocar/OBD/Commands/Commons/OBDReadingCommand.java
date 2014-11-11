package pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Commons;

import java.util.List;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.OBDCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.OBDUnit;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDUnexpectedResponseException;

/**
 * Created by michal on 09.11.14.
 */
public abstract class OBDReadingCommand<T> extends OBDCommand {
    protected abstract T doCalculations(List<Integer> res);

    protected OBDReadingCommand (String command, OBDUnit unit){ super(command, unit); }

    @Override
    protected boolean read(byte[] in) throws OBDUnexpectedResponseException {
        try{
            List<Integer> converted = convertToIntArray(in);
            T result = doCalculations(converted);
            this.result.setValue(result);
            return true;
        }catch (IllegalArgumentException e){
            throw new OBDUnexpectedResponseException(in);
        }
    }
}
