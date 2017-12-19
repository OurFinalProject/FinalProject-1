package com.example.a29751.finalproject;
/**
 * Date: 2017-10-31
 * Finished by Hao Fanyu
 * This is about Automobile
 */

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Automobile extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "Automoblie";

    /*final EditText edF = (EditText)findViewById(R.id.editText);
    final EditText edP = (EditText)findViewById(R.id.editText2);
    final EditText edK = (EditText)findViewById(R.id.editText3);*/
    public AutoDatabaseHelper dbH;
    public SQLiteDatabase db;
    public ArrayList<String> info=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);

        Button btSave = (Button)findViewById(R.id.buttonSave);


        dbH = new AutoDatabaseHelper(this);
        db = dbH.getWritableDatabase();



        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues time = new ContentValues();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat saveDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sDate = saveDate.format(c.getTime());

                info.add(sDate);
                time.put(dbH.KEY_DATE, sDate);
                db.insert(dbH.TABLE_NAME, null, time);

                /*String saveF = edF.getText().toString();
                String saveP = edP.getText().toString();
                String saveK = edK.getText().toString();

                info.add(saveF);
                info.add(saveP);
                info.add(saveK);*/

                Intent intent = new Intent(Automobile.this, AutomobileListview.class);
                startActivity(intent);
            }
        });
        Log.i(ACTIVITY_NAME, "Returned to Automoblie");

    }


}
