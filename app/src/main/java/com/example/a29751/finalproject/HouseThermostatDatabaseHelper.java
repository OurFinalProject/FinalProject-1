package com.example.a29751.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Yan on 2017-11-01.
 */

public class HouseThermostatDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "Message.db";
    private static final int VERSION_NUM = 1;
    public static final String KEY_ID = "id";
    public static final String WEEK_MESSAGE = "week";
    public static final String TIME_MESSAGE = "time";
    public static final String TEMP_MESSAGE = "temp";
 //   public static final String TEMP_WORD = " Temp -> ";
    public static String TABLE_NAME = "HouseThermostatInfo";
    protected static final String ACTIVITY_NAME = "HouseThermostatDBHelper";

    public HouseThermostatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MSG = "CREATE TABLE " + TABLE_NAME  + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WEEK_MESSAGE + " TEXT, "
                + TIME_MESSAGE + " TEXT, "
   //             + TEMP_WORD + " TEXT, "
                + TEMP_MESSAGE + " TEXT )";
        db.execSQL(CREATE_TABLE_MSG);
        Log.i(ACTIVITY_NAME, "Calling onCreate");
        // db.execSQL("create table   "+  TABLE_NAME +(KEY_ID +"   INTEGER AUTOINCREMENT not null,   "+WEEK_MESSAGE+"    text   )"));
    }

    /*
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +
                "(KEY_ID integer primary key autoincrement, " +
                "WEEK_MESSAGE text)");
        Log.i(ACTIVITY_NAME, "Calling onCreate");
    }
    */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
        Log.i(ACTIVITY_NAME, "Calling onDowngrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        Log.i("Database ", "onOpen was called");
    }
}
