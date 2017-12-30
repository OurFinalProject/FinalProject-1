package com.example.a29751.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FoodHistoryList extends AppCompatActivity {


    private ArrayList<String> flistAllItem = new ArrayList<>();
    private ArrayList<String> fnameList = new ArrayList<>();
    private ArrayList<String> fcalsList = new ArrayList<>();
    private ArrayList<String> ffatsList = new ArrayList<>();
    private ArrayList<String> fCarboList = new ArrayList<>();
    private ArrayList<String> fDateList = new ArrayList<>();

    private String[] foodSplitString;
    public SQLiteDatabase fdbSQLite;
    private TextView calDetails, fatDetails,carboDetails,dateDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_history_list);
        ListView listView = (ListView)findViewById(R.id.foodList);
        final FoodListAdapter foodlistAdapter = new FoodListAdapter(this);
        listView.setAdapter(foodlistAdapter);

        foodDatabaseHelper foodData = new foodDatabaseHelper(this);
        fdbSQLite = foodData.getWritableDatabase();

        flistAllItem = foodData.readItem(fdbSQLite);

        for(int i=0;i<flistAllItem.size();i++){
            foodSplitString= flistAllItem.get(i).split("_");
            fnameList.add(foodSplitString[0]);
            fcalsList.add(foodSplitString[1]);
            ffatsList.add(foodSplitString[2]);
            fCarboList.add(foodSplitString[3]);
            fDateList.add(foodSplitString[4]);
        }
        foodlistAdapter.notifyDataSetChanged();

        calDetails = (TextView)findViewById(R.id.fd_detailcals);
        fatDetails = (TextView)findViewById(R.id.fd_detailfats);
        carboDetails = (TextView)findViewById(R.id.fd_detailcabos);
        dateDetails = (TextView)findViewById(R.id.fd_detailDate);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //intent to a fragment detail activity

                Toast t = Toast.makeText(FoodHistoryList.this,"Click on "+foodlistAdapter.getItem(position) , Toast.LENGTH_SHORT);
                t.show();
                calDetails.setText(fcalsList.get(position));
                fatDetails.setText(ffatsList.get(position));
                carboDetails.setText(fCarboList.get(position));
                dateDetails.setText(fDateList.get(position));

            }
        });


    }


    private class FoodListAdapter extends ArrayAdapter<String> {
        public FoodListAdapter(Context ctx){
            super(ctx,0);
        }

        public int getCount(){
            return fnameList.size();
        }

        public String getItem(int position){
            return fnameList.get(position);
        }

        public View getView(int position, View converView, ViewGroup parent){
            LayoutInflater inflater = FoodHistoryList.this.getLayoutInflater();
            View result = null;
            result = inflater.inflate(R.layout.activity_foodname_listview, null);
            TextView foodName = (TextView)result.findViewById(R.id.food_nametext);
        //    result.setBackgroundResource(R.color.colorPrimary);

            foodName.setText(getItem(position));
            return result;
        }

       /* public long getItemId(int position){
            cur.moveToPosition(position);
            return cur.getLong(cur.getColumnIndex(ChatDatabaseHelper.ID_HEADER));
        }*/
    }
}
