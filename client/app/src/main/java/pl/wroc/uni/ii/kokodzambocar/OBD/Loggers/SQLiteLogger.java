package pl.wroc.uni.ii.kokodzambocar.OBD.Loggers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import pl.wroc.uni.ii.kokodzambocar.Api.Measurement;
import pl.wroc.uni.ii.kokodzambocar.Api.Session;
import pl.wroc.uni.ii.kokodzambocar.Database.OBDDatabase;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.OBDCommand;

/**
 * Created by michal on 10.11.14.
 */
public class SQLiteLogger extends OBDDatabase implements OBDLogger{
    private ArrayList<Measurement> mMeasurements = new ArrayList<Measurement>();
    private Session session;
    public SQLiteLogger(Context ctx, Session session) {
        super(ctx);
        this.session = session;
    }

    @Override
    public void log(OBDCommand command) {
        String value = "";
        if (command.getResult() != null && command.getResult().getFormattedValue() != null)
            value = command.getResult().getFormattedValue().toString();
        else
            value = command.getRawResult();
        Measurement m = new Measurement(session.id, OBDId.getId(command), value);
        mMeasurements.add(m);

        if (mMeasurements.size() >= 25);
            addToDatabase();
    }

    @Override
    public void finalize() {
        addToDatabase();
    }

    private void addToDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Measurement> unsuccessfullMeasurements = new ArrayList<Measurement>();
        for (Measurement m : mMeasurements) {
            if (!insert(db, m))
                unsuccessfullMeasurements.add(m);
        }
        mMeasurements = unsuccessfullMeasurements;
        db.close();
    }
}
