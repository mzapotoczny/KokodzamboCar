package pl.wroc.uni.ii.kokodzambocar.OBD;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.OBDCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDBufferOverflowException;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDException;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDInitializationException;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDInputException;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDOutputException;
import pl.wroc.uni.ii.kokodzambocar.OBD.Loggers.OBDLogger;

/**
 * Created by michal on 07.11.14.
 */

public class OBDThread extends Thread {
    private static final String LOG_TAG = "OBDThread";
    private static final String SPP_UUID  = "00001101-0000-1000-8000-00805F9B34FB";

    private final BluetoothDevice mmDevice;
    private BluetoothSocket mSocket;

    private final BluetoothStatusInterface mmCallback;
    private final OBDLogger mmLogger;

    private InputStream mInStream;
    private OutputStream mOutStream;

    private final BlockingQueue<OBDCommand> mTasks;

    private boolean mCancelled;

    public OBDThread(BluetoothDevice device, BluetoothStatusInterface callback, OBDLogger logger)
           throws IOException
    {
        mmDevice = device;
        mmCallback = callback;
        mTasks = new LinkedBlockingQueue<OBDCommand>();
        mCancelled = false;
        mmLogger = logger;

        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));
        mSocket = socket;
    }

    private boolean connect() {
        try {
            mSocket.connect();
            if (createStreams()) {
                mmCallback.connected();
                return true;
            }
        }catch (IOException e) {
            mmCallback.error(new OBDInitializationException());
        }

        return false;
    }

    private boolean createStreams() {
        try {
            mInStream = mSocket.getInputStream();
            mOutStream = mSocket.getOutputStream();
            return true;
        } catch (IOException e) {
            mmCallback.error(new OBDInitializationException());
        }
        return false;
    }

    public void run() {
        connect();
        while ( !mCancelled){
            try{
                OBDCommand command = mTasks.take();
                performCommand(command);
                mmLogger.log(command);
            }catch (InterruptedException e) {
                Log.e(LOG_TAG, "Interrupted exception occurred on take");
            }
        }
    }

    private void performCommand(OBDCommand command) throws InterruptedException{
        try {
            command.perform(mInStream, mOutStream);
        }catch (OBDException e) {
            mmCallback.error(e);
        }finally{
            if (command.isCyclical)
                mTasks.put(command);
        }
    }

    public boolean queue(OBDCommand command){
        return mTasks.add(command);
    }

    public void cancel() {
        try {
            mSocket.close();
            mCancelled = true;
        } catch (IOException e) {
            mmCallback.error(new OBDInitializationException());
        }
    }
}
