package pl.wroc.uni.ii.kokodzambocar;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class Status extends ActionBarActivity implements BluetoothInterface {
    private static int kBluetoothRequest = 1;
    private static String kSPP_UUID =  "00001101-0000-1000-8000-00805F9B34FB";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice[] mDevices;
    private BluetoothThread mmBluetoothThread;

    private enum StatusType { STATUS_SUCCESS, STATUS_WAITING, STATUS_FAILURE };

    private TextView mStatusTextView;
    private EditText mUserInput;

    private static String[] commands = {"AT Z", "AT E0", "AT E0", "AT L0", "AT SP 0", "01 05"};
    private static int currCommand = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        this.mStatusTextView = (TextView) findViewById(R.id.statusText);
        this.mUserInput = (EditText) findViewById(R.id.sendText);
        initalizeBluetooth();
    }

    public void sendUserInput(View view) {
        String input = this.mUserInput.getText().toString();
        input = input + "\r\n";
        mmBluetoothThread.write(input.getBytes());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setStatus(String text, StatusType type){
        int color = Color.BLACK;
        switch (type){
            case STATUS_SUCCESS:
                color = Color.GREEN;
                break;
            case STATUS_WAITING:
                color = Color.BLUE;
                break;
            case STATUS_FAILURE:
                color = Color.RED;
                break;
        }
        this.mStatusTextView.setTextColor(color);
        this.mStatusTextView.setText(text);
    }

    private void initalizeBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null){
            this.setStatus("Could not initialize bluetooth", StatusType.STATUS_FAILURE);
            return;
        }

        if (mBluetoothAdapter.isEnabled()){
            this.chooseDevice();
        }else{
            this.setStatus("Waiting for bluetooth", StatusType.STATUS_WAITING);

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, kBluetoothRequest);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == kBluetoothRequest){
            if (resultCode == RESULT_OK)
                this.chooseDevice();
            else
                this.setStatus("Bluetooth not enabled", StatusType.STATUS_FAILURE);
        }
    }

    private void chooseDevice() {
        this.setStatus("Waiting for device choose", StatusType.STATUS_WAITING);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose device");
        builder.setItems(this.getDeviceNames(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setStatus("Chosen device: " + mDevices[which].getName() + " (" + mDevices[which].getAddress() + ")", StatusType.STATUS_SUCCESS);
                connectToDevice(mDevices[which]);
            }
        });
        builder.create().show();
    }

    private void connectToDevice(BluetoothDevice device) {
        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString(kSPP_UUID));
            if (socket != null){
                setStatus("Socket created, waiting for connection", StatusType.STATUS_WAITING);

                mmBluetoothThread = new BluetoothThread(socket, this);
                mmBluetoothThread.start();
            }else{
                setStatus("Could not create socket", StatusType.STATUS_FAILURE);
            }
        }catch (IOException exception){
            setStatus("IOException: "+exception.getLocalizedMessage(), StatusType.STATUS_FAILURE);
        }
    }

    private String[] getDeviceNames() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        mDevices = pairedDevices.toArray(new BluetoothDevice[pairedDevices.size()]);
        String[] devices = new String[mDevices.length];
        for (int i = 0; i < mDevices.length; i++) {
            devices[i] = mDevices[i].getName();
        }
        return devices;
    }


    /* Bluetooth interface methods */
    public void connected() {
        setStatus("Connected", StatusType.STATUS_SUCCESS);
        receiveMessage(null, 0);
    }

    public void error(BluetoothError reason){
        String str = "";
        switch (reason){
            case CLOSE:
                str = "close";
                break;
            case CONNECTION:
                str = "connection";
                break;
            case READ:
                str = "read";
                break;
            case WRITE:
                str = "write";
                break;
            case STREAM:
                str = "stream";
        }
        setStatus("Error: "+str, StatusType.STATUS_FAILURE);
    }

    public synchronized void receiveMessage(byte[] buffer, int length) {
        byte[] buff = new byte[length];
        for (int i = 0; i < length; i++)
            buff[i] = buffer[i];
        String s = new String(buff);
        Log.i("receiveMessage", "Received message "+s.replace('\r', 'R').replace('\n', 'N'));
        if (currCommand < commands.length){
            String msg = commands[currCommand++];
            Log.i("sendMessage", msg);
            msg = msg + "\r";
            mmBluetoothThread.write(msg.getBytes());
        }
    }
}
