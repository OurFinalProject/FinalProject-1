package com.example.a29751.finalproject;
/**
 * Date: 2017-10-31
 * Finished by Du Liangliang
 * This is about Food Nutrition information tracker
 */
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodNutrition extends AppCompatActivity {

    private TextView fName,cals,fats,carbos;
    public foodDatabaseHelper foodData;
    public SQLiteDatabase fdbSQLite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition);

        fName = (TextView)findViewById(R.id.foodName);
        cals = (TextView)findViewById(R.id.fd_cals);
        fats = (TextView)findViewById(R.id.fd_fats);
        carbos = (TextView)findViewById(R.id.fd_cbs);
        Button btnSave = (Button)findViewById(R.id.fd_saveBtn);

        foodData = new foodDatabaseHelper(this);
        fdbSQLite = foodData.getWritableDatabase();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String foodName = fName.getText().toString();

                foodData.saveItem(fdbSQLite,foodName,cals.getText().toString(),
                       fats.getText().toString(),carbos.getText().toString());

                Intent intent = new Intent(FoodNutrition.this, FoodHistoryList.class);

                startActivity(intent);
            }
        });


    }
}
