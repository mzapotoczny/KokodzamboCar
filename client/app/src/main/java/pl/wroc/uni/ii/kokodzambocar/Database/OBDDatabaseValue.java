package pl.wroc.uni.ii.kokodzambocar.Database;

import android.database.Cursor;

/**
 * Created by michal on 25.01.15.
 */
public class OBDDatabaseValue {
    public Integer id;
    public Integer session;
    public Integer pid;
    public String text;
    public boolean sent;

    static OBDDatabaseValue fromCursor(Cursor c) {
        OBDDatabaseValue dbVal = new OBDDatabaseValue();
        dbVal.id = c.getInt(0);
        dbVal.session = c.getInt(1);
        dbVal.pid = c.getInt(2);
        dbVal.text = c.getString(3);
        dbVal.sent = (c.getInt(4) > 0);
        return dbVal;
    }
}
