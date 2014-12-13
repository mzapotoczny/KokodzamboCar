package pl.wroc.uni.ii.kokodzambocar.Activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import pl.wroc.uni.ii.kokodzambocar.OBD.BluetoothStatusInterface;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine.OBDEngineLoadCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine.OBDEngineRuntimeCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine.OBDMAFCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine.OBDRPMCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine.OBDThrottlePositionCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.OBDCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.OBDCompletionHandler;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Proto.OBDAutoProtocolCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Proto.OBDEchoOffCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Proto.OBDLineFeedOffCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Proto.OBDResetCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Proto.OBDSupportedPIDsCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Temperature.OBDAirIntakeTemperatureCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Temperature.OBDAmbientAirTemperature;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Temperature.OBDEngineCoolantTemperature;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDException;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDUnexpectedResponseException;
import pl.wroc.uni.ii.kokodzambocar.OBD.Loggers.SQLiteLogger;
import pl.wroc.uni.ii.kokodzambocar.OBD.OBDThread;
import pl.wroc.uni.ii.kokodzambocar.R;


public class Status extends ActionBarActivity implements BluetoothStatusInterface {
    private static int kBluetoothRequest = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice[] mDevices;
    private OBDThread mmOBDThread;

    private enum StatusType { STATUS_SUCCESS, STATUS_WAITING, STATUS_FAILURE };

    private TextView mStatusTextView;
    private LinearLayout mLinearLayout;
    private final ArrayList<TextView> mLinearTextViews = new ArrayList<TextView>();
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        this.mStatusTextView = (TextView) findViewById(R.id.statusText);
        this.mLinearLayout   = (LinearLayout) findViewById(R.id.linearLayout);

        initializeBluetooth();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mmOBDThread != null){
            mmOBDThread.cancel();
        }
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

    private void initializeBluetooth() {
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
            mmOBDThread = new OBDThread(device, this, new SQLiteLogger(this));
            setStatus("Connecting...", StatusType.STATUS_WAITING);
            mmOBDThread.start();
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

    private void initializeCommunication(){
        mmOBDThread.queue(getOBDCommandWithStatus(new OBDResetCommand()));
        mmOBDThread.queue(getOBDCommandWithStatus(new OBDEchoOffCommand()));
        mmOBDThread.queue(getOBDCommandWithStatus(new OBDEchoOffCommand()));
        mmOBDThread.queue(getOBDCommandWithStatus(new OBDLineFeedOffCommand()));
        mmOBDThread.queue(getOBDCommandWithStatus(new OBDAutoProtocolCommand()));
        mmOBDThread.queue(getOBDCommandWithStatus(new OBDSupportedPIDsCommand()));

        mmOBDThread.queue(getOBDCommandWithStatusAndValue(new OBDRPMCommand()));
        mmOBDThread.queue(getOBDCommandWithStatusAndValue(new OBDEngineLoadCommand()));
        mmOBDThread.queue(getOBDCommandWithStatusAndValue(new OBDMAFCommand()));
        mmOBDThread.queue(getOBDCommandWithStatusAndValue(new OBDThrottlePositionCommand()));
        mmOBDThread.queue(getOBDCommandWithStatusAndValue(new OBDEngineRuntimeCommand()));
        mmOBDThread.queue(getOBDCommandWithStatusAndValue(new OBDAirIntakeTemperatureCommand()));
        mmOBDThread.queue(getOBDCommandWithStatusAndValue(new OBDAmbientAirTemperature()));
        mmOBDThread.queue(getOBDCommandWithStatusAndValue(new OBDEngineCoolantTemperature()));
    }

    private OBDCommand getOBDCommandWithStatus(OBDCommand command){
        command.setCompletionHandler(new OBDCompletionHandler() {
            @Override
            public void before(OBDCommand command) {
                final String statusString = command.name() + " ...";
                mMainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setStatus(statusString, StatusType.STATUS_WAITING);
                    }
                });
            }

            @Override
            public boolean error(OBDCommand command, OBDException exception) {
                return !(exception.getClass() == OBDUnexpectedResponseException.class);
            }

            @Override
            public void done(OBDCommand command) {
                final String statusString = command.name() + " done";
                mMainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setStatus(statusString, StatusType.STATUS_SUCCESS);
                    }
                });
            }
        });
        return command;
    }

    private OBDCommand getOBDCommandWithStatusAndValue(OBDCommand command){
        final int id = mLinearTextViews.size();
        final TextView textView = new TextView(this);

        textView.setText(command.name() + ": NO DATA");
        mLinearLayout.addView(textView);
        mLinearTextViews.add(textView);

        command.isCyclical = true;
        command.setCompletionHandler(new OBDCompletionHandler() {
            @Override
            public void before(OBDCommand command) {
                mMainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLinearTextViews.get(id).setTextColor(Color.GREEN);
                    }
                });
            }

            @Override
            public boolean error(OBDCommand command, OBDException exception) {
                return !(exception.getClass() == OBDUnexpectedResponseException.class);
            }

            @Override
            public void done(OBDCommand command) {
                final String statusString = command.name() + " done";
                final String valueString  = command.name() + ": " + command.getResult().getFormattedValue();
                mMainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setStatus(statusString, StatusType.STATUS_SUCCESS);
                        mLinearTextViews.get(id).setTextColor(Color.BLACK);
                        mLinearTextViews.get(id).setText(valueString);
                    }
                });
            }
        });
        return command;
    }

    /* Bluetooth interface methods */
    public void connected() {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                setStatus("Connected", StatusType.STATUS_SUCCESS);
                initializeCommunication();
            }
        });
    }

    public void error(final OBDException error){
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                setStatus("Error: "+error.getMessage(), StatusType.STATUS_FAILURE);
            }
        });
    }

    public void disconnected() {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                setStatus("Disconnected", StatusType.STATUS_SUCCESS);
            }
        });
    }
}
