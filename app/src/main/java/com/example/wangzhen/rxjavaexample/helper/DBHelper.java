package com.example.wangzhen.rxjavaexample.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by wangzhen on 17/3/4.
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "tracker.db";

    private static volatile DBHelper instance;

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table log_data " +
                        "(id integer PRIMARY KEY AUTOINCREMENT" +
                        ", content text not null" +
                        ", createAt Long not null)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS log_data");
        onCreate(db);
    }

    public boolean save(String logData) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "insert into log_data(content, createAt) values (?, ?);";
        Object[] bindArgs = new Object[]{logData, new Date().getTime()};
        db.execSQL(sql, bindArgs);
        return true;
    }

    public Integer getLastLogDataId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select id from log_data order by id asc limit 1";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            } else {
                return null;
            }
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public String getLogDataById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select content from log_data where id = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            } else {
                return null;
            }
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public boolean deleteLogDataById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = db.delete("log_data", "id = ? ", new String[]{String.valueOf(id)});
        return count > 0;
    }
}
