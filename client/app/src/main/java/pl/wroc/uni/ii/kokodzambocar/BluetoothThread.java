package pl.wroc.uni.ii.kokodzambocar;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by michal on 07.11.14.
 */

public class BluetoothThread extends Thread implements BluetoothInterface {
    private final BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private Handler mHandler;
    private final BluetoothInterface mmCallback;

    private enum MessageType { CONNECTED, ERROR, RECEIVE };

    MessageType[] messageTypeValues;
    BluetoothError[] bluetoothErrorValues;

    public BluetoothThread(BluetoothSocket socket, BluetoothInterface callback) {
        messageTypeValues = MessageType.values();
        bluetoothErrorValues = BluetoothError.values();
        mmSocket = socket;
        mmCallback = callback;
        mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (messageTypeFromInt(msg.what)){
                    case CONNECTED:
                        mmCallback.connected();
                        break;
                    case ERROR:
                        mmCallback.error(bluetoothErrorFromInt(msg.arg1));
                        break;
                    case RECEIVE:
                        byte[] buffer = (byte[])msg.obj;
                        mmCallback.receiveMessage(buffer, msg.arg1);
                        break;
                }

                return false;
            }
        });
    }

    private MessageType messageTypeFromInt(int msg){
        if (msg >= 0 && msg < messageTypeValues.length)
            return messageTypeValues[msg];
        else
            return null;
    }

    private BluetoothError bluetoothErrorFromInt(int msg){
        if (msg >= 0 && msg < bluetoothErrorValues.length)
            return bluetoothErrorValues[msg];
        else
            return null;
    }

    private boolean connect() {
        try {
            mmSocket.connect();
            if (createStreams()) {
                connected();
                return true;
            }
        }catch (IOException e) {
            error(BluetoothInterface.BluetoothError.CONNECTION);
        }

        return false;
    }

    private boolean createStreams() {
        try {
            mmInStream = mmSocket.getInputStream();
            mmOutStream = mmSocket.getOutputStream();
            return true;
        } catch (IOException e) {
            error(BluetoothInterface.BluetoothError.STREAM);
        }
        return false;
    }

    private void receive(){
        byte[] buffer = new byte[1024];
        int bytes = 0;

        while (true) {
            try {
                if (bytes > 0){
                    // bytes + 1?
                    int mbytes = mmInStream.read(buffer, bytes, 1024 - bytes);
                    bytes += mbytes;
                }else {
                    bytes = mmInStream.read(buffer);
                }
                int bt = (int) buffer[bytes-1];
                String bts = String.valueOf(bt);

                if (bytes >= 3 && buffer[bytes-3] == '\r' && buffer[bytes-2] == '\r' && buffer[bytes-1] == '>') {
                    receiveMessage(buffer, bytes-3);
                    bytes = 0;
                }
            } catch (IOException e) {
                error(BluetoothInterface.BluetoothError.READ);
                break;
            }
        }
    }

    public void run() {
        if (connect())
            receive();
    }

    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            error(BluetoothInterface.BluetoothError.WRITE);
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            error(BluetoothInterface.BluetoothError.CLOSE);
        }
    }

    public void connected() {
        mHandler.obtainMessage(MessageType.CONNECTED.ordinal()).sendToTarget();
    }

    public void error(BluetoothError reason) {
        mHandler.obtainMessage(MessageType.ERROR.ordinal(), reason.ordinal()).sendToTarget();
    }

    public void receiveMessage(byte[] buffer, int length) {
        mHandler.obtainMessage(MessageType.RECEIVE.ordinal(), length, -1, buffer).sendToTarget();
    }
}
