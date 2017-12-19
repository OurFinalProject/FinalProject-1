package com.example.a29751.finalproject;
/**
 * Date: 2017-10-31
 * Created by Liangliang Du
 * Teamwork: Cheng Yan & Wang Yongdan & Du Liangliang & Hao Fanyu
 * This is the main interface
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView activityTrack = (TextView) findViewById(R.id.activityLaunch);//wang
        TextView foodNutrition = (TextView) findViewById(R.id.FoodLaunch);//du
        TextView houseThermostat = (TextView) findViewById(R.id.HouseLaunch);//cheng
        TextView autoMobile = (TextView) findViewById(R.id.autolaunch);//hao

        activityTrack.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(MainActivity.this,ActivityTracking.class));
          }
        });

        foodNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FoodNutrition.class));
            }
        });

        houseThermostat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HouseThermostat.class));
            }
        });

        autoMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Automobile.class));
            }
        });
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
