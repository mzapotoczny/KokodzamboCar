package pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions;

/**
 * Created by michal on 09.11.14.
 */
public class OBDBufferOverflowException extends OBDException{
    public OBDBufferOverflowException(String command, byte[] buffer) {
        super("Buffer overflow for command "+command);
    }
}
