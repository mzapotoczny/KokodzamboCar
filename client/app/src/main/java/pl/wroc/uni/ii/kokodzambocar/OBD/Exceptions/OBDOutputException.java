package pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions;

/**
 * Created by michal on 09.11.14.
 */
public class OBDOutputException extends OBDException {
    public OBDOutputException(String msg) {
        super("Output exception for "+msg);
    }
}
