package pl.wroc.uni.ii.kokodzambocar.OBD;

import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDException;

/**
 * Created by michal on 07.11.14.
 */
public interface BluetoothStatusInterface {
    public void connected();
    public void disconnected();
    public void error(OBDException error);
}
