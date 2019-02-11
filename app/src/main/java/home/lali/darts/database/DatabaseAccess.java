package home.lali.darts.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context){
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context){
        if (instance == null){
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open(){
        this.database = openHelper.getWritableDatabase();
    }

    public void close(){
        if (database != null){
            this.database.close();
        }
    }

    public String getCheckout(String text){
        Cursor cursor = database.rawQuery("SELECT finish FROM CHECKOUTS WHERE number = " + text, null);
        if (cursor.getCount() == 0){
            return "";
        }
        cursor.moveToNext();
        String checkout_str = cursor.getString(0);
        cursor.close();
        return checkout_str;
    }

    public void saveLocalGameStat(String p1name, int p1L, double p1Avg, String p2name, int p2L, double p2Avg) {
        try {
            String sql = "INSERT INTO LocalStats (p1Name, p1Leg, p1Avg, p2Name, p2Leg, p2Avg) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            SQLiteStatement stmt = database.compileStatement(sql);

            stmt.bindString(1, p1name);
            stmt.bindLong(2, p1L);
            stmt.bindDouble(3, p1Avg);
            stmt.bindString(4, p2name);
            stmt.bindLong(5, p2L);
            stmt.bindDouble(6, p2Avg);

            stmt.executeInsert();

            stmt.close();
        } catch (Exception e) {
            Log.e("Insert Exception", e.getMessage());
        }
    }

    public Cursor getLocalResults() {
        return database.rawQuery("SELECT * FROM LocalStats", null);
    }
}