package pl.wroc.uni.ii.kokodzambocar;

/**
 * Created by michal on 07.11.14.
 */
public interface BluetoothStatusInterface {
    public enum  BluetoothError {CONNECTION, STREAM, WRITE, READ, CLOSE};

    public void connected();
    public void error(BluetoothError reason);
}
