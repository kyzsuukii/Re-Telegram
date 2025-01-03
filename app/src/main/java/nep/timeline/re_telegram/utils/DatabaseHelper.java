package nep.timeline.re_telegram.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = ".settings.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAMES = "file_names";

    private static final String KEY_NAME = "name";
    private static final String KEY_VALUE = "value";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAMES + "("
                + KEY_NAME + " TEXT PRIMARY KEY,"
                + KEY_VALUE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES);
        onCreate(db);
    }

    public void setValue(String name, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_VALUE, value);
        db.insertWithOnConflict(TABLE_NAMES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public String getValue(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAMES, new String[]{KEY_VALUE},
                KEY_NAME + "=?", new String[]{name},
                null, null, null);

        String value = null;
        if (cursor.moveToFirst()) {
            value = cursor.getString(0);
        }
        cursor.close();
        return value;
    }
}
