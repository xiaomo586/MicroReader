package name.caiyao.microreader.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import name.caiyao.microreader.config.Config;

/**
 * Created by 蔡小木 on 2016/4/14 0014.
 */
public class DBUtils {
    private static DBUtils sDBUtis;
    private SQLiteDatabase mSQLiteDatabase;

    private DBUtils(Context context) {
        mSQLiteDatabase = new DBHelper(context, Config.DB_NAME + ".db").getWritableDatabase();
    }

    public static synchronized DBUtils getDB(Context context) {
        if (sDBUtis == null)
            sDBUtis = new DBUtils(context);
        return sDBUtis;
    }


    public void insertHasRead(String table, String key, int value) {
        Cursor cursor = mSQLiteDatabase.query(table, null, null, null, null, null, "id asc");
        if (cursor.getCount() > 50) {
            if (cursor.moveToNext())
                mSQLiteDatabase.delete(table, "id=?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("id")))});
        }
        cursor.close();
        ContentValues contentValues = new ContentValues();
        contentValues.put("key", key);
        contentValues.put("is_read", value);
        mSQLiteDatabase.insert(table, null, contentValues);
    }

    public boolean isRead(String table, String key, int value) {
        boolean isRead = false;
        Cursor cursor = mSQLiteDatabase.query(table, null, "key=?", new String[]{key}, null, null, null);
        if (cursor.moveToNext() && (cursor.getInt(cursor.getColumnIndex("is_read")) == value)) {
            isRead = true;
        }
        cursor.close();
        return isRead;
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name) {
            super(context, name, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table if not exists " + Config.GUOKR + "(id integer primary key autoincrement,key text,is_read integer);");
            db.execSQL("create table if not exists " + Config.IT + "(id integer primary key autoincrement,key text,is_read integer)");
            db.execSQL("create table if not exists " + Config.VIDEO + "(id integer primary key autoincrement,key text,is_read integer)");
            db.execSQL("create table if not exists " + Config.ZHIHU + "(id integer primary key autoincrement,key text,is_read integer)");
            db.execSQL("create table if not exists " + Config.WEIXIN + "(id integer primary key autoincrement,key text,is_read integer)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}