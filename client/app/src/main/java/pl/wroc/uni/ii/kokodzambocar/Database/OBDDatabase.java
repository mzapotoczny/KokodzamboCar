package pl.wroc.uni.ii.kokodzambocar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import pl.wroc.uni.ii.kokodzambocar.Api.Measurement;
import pl.wroc.uni.ii.kokodzambocar.Api.Session;

/**
 * Created by michal on 25.01.15.
 */
public class OBDDatabase extends SQLiteOpenHelper {
    protected static String tableName = "obd";
    public OBDDatabase(Context ctx) {
        super(ctx, "kokodzamboCarOBDData.db", null, 1);
    }

    protected boolean insert(SQLiteDatabase db, Measurement measurement) {
        ContentValues value = new ContentValues();
        value.put("session", measurement.session);
        value.put("pid", measurement.pid);
        value.put("value", measurement.value);
        return (db.insert(tableName, null, value) > -1);
    }

    protected List<OBDDatabaseValue> getValues(SQLiteDatabase db) {
        String query = "SELECT * FROM "+tableName;
        Cursor c = db.rawQuery(query, null);
        ArrayList<OBDDatabaseValue> retList = new ArrayList<OBDDatabaseValue>();
        if (c.moveToFirst()){
            do {
                retList.add(OBDDatabaseValue.fromCursor(c));
            }while (c.moveToNext());
        }
        return retList;
    }

    protected void remove(SQLiteDatabase db, Integer id) {
        db.delete(tableName, "id = ?", new String[] {id.toString()});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+tableName+" (" +
                "id integer primary key autoincrement," +
                "session integer," +
                "pid integer," +
                "value text," +
                "sent integer default 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

