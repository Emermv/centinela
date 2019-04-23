package com.centinela.storage;

import android.content.Context; import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version); }
public DbHelper(Context context) {
    // null porque se va a usar el SQLiteCursor
    super(context, "centinela.db", null, 1);
} @Override public void onCreate(SQLiteDatabase db) {

    String sql = "CREATE TABLE IF NOT EXISTS settings (id INTEGER PRIMARY KEY AUTOINCREMENT, name varchar(200) NOT NULL, value varchar(200))";
    db.execSQL(sql);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    db.execSQL("DROP TABLE IF EXISTS settings");
    onCreate(db);
}



    }