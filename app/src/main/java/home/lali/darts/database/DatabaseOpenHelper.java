package home.lali.darts.database;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

class DatabaseOpenHelper extends SQLiteAssetHelper {

    private static final String DB_NAME = "dartsdb.db";
    private static final int DB_VERSION = 1;

    DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
}