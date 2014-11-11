package pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions;

/**
 * Created by michal on 09.11.14.
 */
public class OBDUnexpectedResponseException extends OBDException{
    public OBDUnexpectedResponseException(byte[] in) {
        super("Unexpected response: "+(new String(in)));
    }
}
