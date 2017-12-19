package com.example.a29751.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by haofa on 2017-12-03.
 */

public class AutoDatabaseHelper extends SQLiteOpenHelper {


    protected static final String DATABASE_NAME = "data";
    protected static final String TABLE_NAME = "auto";
    protected static final int VERSION_NUM = 1;
    protected static final String KEY_ID = "ID";
    /*protected static final String KEY_MESSAGE = "Message";*/
    protected static final String KEY_DATE = "Date";

    public AutoDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_DATE + " TEXT " + ")";
        db.execSQL(CREATE_TABLE);
        Log.i("AutoDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {

        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);

    }
}
