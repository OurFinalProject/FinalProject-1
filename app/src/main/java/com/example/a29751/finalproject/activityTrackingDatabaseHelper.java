package com.example.a29751.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sowha on 2017-11-28.
 */

public class activityTrackingDatabaseHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME="FinalProject.db";
    static int VERSION_NUM=2;
    public final static String TABLE_NAME="activity";
    public final static String KEY_ID="_id";
    public final static String KEY_TYPE="TYPE";
    public final static String KEY_TIME="TIME";
    public final static String KEY_COMMENTS="COMMENTS";
    public final static String KEY_DATE="DATE";

    public activityTrackingDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("ChatDatabaseHelper", "Calling onCreate");
        String CREATE_TABLE_MSG = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_TYPE + " TEXT ," +  KEY_TIME + " INTEGER ,"
                +  KEY_COMMENTS + " TEXT ,"
                + KEY_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP )";
        db.execSQL(CREATE_TABLE_MSG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + i + "newVersion=" + i1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
