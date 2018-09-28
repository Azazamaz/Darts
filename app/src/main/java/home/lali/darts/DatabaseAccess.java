package home.lali.darts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    public DatabaseAccess(Context context){
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
}