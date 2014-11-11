package pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions;

/**
 * Created by michal on 09.11.14.
 */
public class OBDInputException extends OBDException {
    public OBDInputException(String command, byte[] buff) {
        super("Input exception for command "+command);
    }
}
