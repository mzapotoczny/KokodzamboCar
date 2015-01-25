package pl.wroc.uni.ii.kokodzambocar.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.wroc.uni.ii.kokodzambocar.Api.Api;
import pl.wroc.uni.ii.kokodzambocar.Api.Measurement;
import pl.wroc.uni.ii.kokodzambocar.Database.OBDDatabase;
import pl.wroc.uni.ii.kokodzambocar.Database.OBDDatabaseValue;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by michal on 25.01.15.
 */
public class Uploader extends Service {

    class UploaderWorker extends OBDDatabase implements Runnable {
        public boolean running;
        private final Integer maxSendout = 100;
        UploaderWorker(Context ctx) { super(ctx); running = true; }

        @Override
        public void run() {
            while (running) {
                try{
                    upload();
                    Thread.sleep(200000);
                }catch (InterruptedException e){
                }
            }
        }

        /* Very bad code, sorry :( */
        public void upload() {
            SQLiteDatabase db = getReadableDatabase();
            List<OBDDatabaseValue> values = getValues(db);
            db.close();
            if (values.size() == 0) {
                return;
            }
            Collections.sort(values, new Comparator<OBDDatabaseValue>() {
                @Override
                public int compare(OBDDatabaseValue lhs, OBDDatabaseValue rhs) {
                    return (lhs.session < rhs.session) ? 1 : 0;
                }
            });
            ArrayList<Integer> sentIds = new ArrayList<Integer>();
            ArrayList<Measurement> currentSend = new ArrayList<Measurement>();
            Integer currentSession = values.get(0).session;
            Integer lastSendId = 0;
            for (Integer currentValueId = 0; currentValueId < values.size(); currentValueId++) {

                OBDDatabaseValue currentValue = values.get(currentValueId);
                if (currentSend.size() >= maxSendout || currentSession != currentValue.session) {
                    if (send(currentSession, currentSend)) {
                        for (Integer i = lastSendId; i < currentValueId; i++) sentIds.add(i);
                    }
                    lastSendId = currentValueId;
                    currentSend.clear();
                }
                currentSend.add(new Measurement(currentValue.pid, currentValue.text));

            }
            if (send(currentSession, currentSend)) {
                for (Integer i = lastSendId; i < values.size(); i++) sentIds.add(i);
            }
            db = getWritableDatabase();
            for (Integer id : sentIds){
                remove(db, values.get(id).id);
            }
        }

        public boolean send(Integer session, List<Measurement> measurements) {
            try {
                Response rsp = Api.getInstance().session.addMeasurements(session, measurements);
            }catch(RetrofitError e){
                return false;
            }
            return true;
        }
    }

    private UploaderWorker mWorker;
    private Thread mWorkerThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mWorker = new UploaderWorker(this);
        mWorkerThread = new Thread(mWorker);
        mWorkerThread.start();

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mWorker.running = false;
        mWorkerThread.interrupt();
    }

}
