package pl.wroc.uni.ii.kokodzambocar.OBD.Loggers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.OBDCommand;

/**
 * Created by michal on 10.11.14.
 */
public class SQLiteLogger extends SQLiteOpenHelper implements OBDLogger{
    public SQLiteLogger(Context ctx) {
        super(ctx, "obd.db", null, 1);
    }

    @Override
    public void log(OBDCommand command) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("pid", OBDId.getId(command));
        value.put("value", command.getResult().getFormattedValue());
        db.insertOrThrow("obd", null, value);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE obd (id integer primary key autoincrement, pid integer, value text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
