package pl.wroc.uni.ii.kokodzambocar.OBD.Commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.NoneUnit;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Units.OBDUnit;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDBufferOverflowException;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDInputException;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDOutputException;
import pl.wroc.uni.ii.kokodzambocar.OBD.Exceptions.OBDUnexpectedResponseException;

/**
 * Created by michal on 09.11.14.
 */
public abstract class OBDCommand {
    private static final int BUFFER_SIZE = 1024;
    protected String command;
    protected String rawResult;
    protected OBDUnit result;

    public boolean isCyclical = false;
    private OBDCompletionHandler mCompletionHandler;

    protected abstract boolean read(byte[] in) throws OBDUnexpectedResponseException;
    public abstract String name();

    protected OBDCommand() {}
    protected OBDCommand(String command) { this.command = command; }
    protected OBDCommand(String command, OBDUnit unit){
        this.command = command;
        result = unit;
    }

    public OBDUnit getResult() {
        return result;
    }

    public String getRawResult() {
        return rawResult;
    }

    public String getCommand() {
        return command;
    }

    public void setCompletionHandler(OBDCompletionHandler completionHandler){
        mCompletionHandler = completionHandler;
    }

    public OBDCompletionHandler getCompletionHandler() {
        return mCompletionHandler;
    }

    public void perform(InputStream input, OutputStream output)
           throws OBDInputException,
                  OBDOutputException,
                  OBDBufferOverflowException,
                  OBDUnexpectedResponseException {
        if (mCompletionHandler != null)
            mCompletionHandler.before(this);
        write(output, command);
        byte[] in = receive(input);
        rawResult = new String(in);
        try {
            read(in);
            if (mCompletionHandler != null)
                mCompletionHandler.done(this);
        }catch (OBDUnexpectedResponseException e){
            if (mCompletionHandler != null) {
                if (!mCompletionHandler.error(this, e))
                    this.isCyclical = false;
            }
        }
    }

    private byte[] receive(InputStream inputStream)
            throws OBDBufferOverflowException,
                   OBDInputException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes = 0;

        while (true) {
            try {
                if (bytes >= BUFFER_SIZE){
                    throw new OBDBufferOverflowException(command, buffer);
                }
                if (bytes > 0){
                    // TODO: timeout
                    int addBytes = inputStream.read(buffer, bytes, BUFFER_SIZE - bytes);
                    bytes += addBytes;
                }else bytes = inputStream.read(buffer);

                if (bytes >= 3 &&
                    buffer[bytes-3] == '\r' &&
                    buffer[bytes-2] == '\r' &&
                    buffer[bytes-1] == '>') {
                    byte[] restBuffer = Arrays.copyOf(buffer, bytes - 3);
                    return restBuffer;
                }
            } catch (IOException e) {
                throw new OBDInputException(command, buffer);
            }
        }
    }

    public void write(OutputStream outputStream, String cmd)
           throws OBDOutputException {
        try {
            String command = cmd + "\r";
            // TODO: Timeout
            outputStream.write(command.getBytes());
            outputStream.flush();

            Thread.sleep(20);
        } catch (IOException e) {
            throw new OBDOutputException(cmd);
        } catch (InterruptedException e) {}
    }

    protected List<Integer> convertToIntArray(byte[] input)
              throws IllegalArgumentException {
        ArrayList<Integer> converted = new ArrayList<Integer>();
        int currentValue = 0;
        for (int i = 0; i < input.length; i++){
            if (input[i] == ' '){
                converted.add(currentValue);
                currentValue = 0;
            }else{
                currentValue *= 16;
                currentValue += hexByteToInt(input[i]);
            }
        }
        return converted;
    }

    private int hexByteToInt(byte b)
            throws IllegalArgumentException {
        int val = b - '0';
        int toReturn;
        if (val <= 9)
            toReturn = val;
        else
            toReturn = 10 + (b - 'A');
        if (toReturn >= 16)
            throw new IllegalArgumentException();
        return toReturn;
    }
}
