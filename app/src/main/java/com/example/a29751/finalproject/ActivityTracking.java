package com.example.a29751.finalproject;
/**
 * Date: 2017-10-31
 * Finished by Wang Yongdan
 * This is about Activity Tracking
 *
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import android.support.design.widget.Snackbar;


import static android.media.CamcorderProfile.get;
import static com.example.a29751.finalproject.activityTrackingDatabaseHelper.TABLE_NAME;

public class ActivityTracking extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ActivityTracking";
    Button saveButton;
    Spinner activitySpinner;
    EditText timeText;
    EditText commentsText;
    ListView listView;
    TextView totalTime;
    activityTrackingDatabaseHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<Activites> messages = new ArrayList<Activites>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        saveButton=(Button)findViewById(R.id.saveButton);
        activitySpinner=(Spinner)findViewById(R.id.spinner);
        timeText=(EditText)findViewById(R.id.editText1);
        commentsText=(EditText)findViewById(R.id.editText2);
        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        //Snackbar snackbar = Snackbar.make(coordinatorlayout, "Welcome to Activity Tracking", Snackbar.LENGTH_LONG);
        //snackbar.show();

        String[] arraySpinner= new String[] {
                "Running", "Walking", "Biking", "Swimming", "Skating"
        };
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        activitySpinner.setAdapter(spinnerAdapter);

        dbHelper = new activityTrackingDatabaseHelper(this) ;
        db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery("select * from " + TABLE_NAME,null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE " + c.getString(c.getColumnIndex(dbHelper.KEY_TYPE)));
            Activites act = new Activites(c.getString(c.getColumnIndex(dbHelper.KEY_TYPE)),
                    c.getInt(c.getColumnIndex(dbHelper.KEY_TIME)),
                    c.getString(c.getColumnIndex(dbHelper.KEY_COMMENTS)));
                     messages.add(act);
            c.moveToNext();
        }

        final ActivityTrackingAdapter adapter =  new ActivityTrackingAdapter(this);
        listView.setAdapter(adapter);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(ActivityTracking.this).create();
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Add new record?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                String type = activitySpinner.getSelectedItem().toString();
                int time=0;
                String comments = commentsText.getText().toString();

                if (!timeText.getText().toString().isEmpty()) {
                    time=Integer.parseInt(timeText.getText().toString());
                }

                if (comments.isEmpty()) {
                    comments = "No comment";
                }
                Activites act = new Activites(type, time, comments);
                messages.add(act);

                ContentValues initialValues = new ContentValues();
                adapter.notifyDataSetChanged();
                //adapter.notifyDataSetChanged();
                initialValues.put(dbHelper.KEY_TYPE,activitySpinner.getSelectedItem().toString());
                initialValues.put(dbHelper.KEY_TIME,timeText.getText().toString());
                initialValues.put(dbHelper.KEY_COMMENTS,commentsText.getText().toString());
                db.insert(TABLE_NAME,null,initialValues);
                timeText.setText("");
                commentsText.setText("");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Activites act = adapter.getActivity(position);

                Toast.makeText(getBaseContext(), act.toString(), Toast.LENGTH_LONG).show();


                Bundle bundle = new Bundle();
                bundle.putString("type",act.getType());
                bundle.putInt("minutes", act.getMinutes());
                bundle.putString("comments",act.getComments());
                bundle.putString("time", act.getTime().toString());

                ActivityTrackingFragment messageFragment = new ActivityTrackingFragment();

                messageFragment.setArguments(bundle);
                FragmentManager fragmentManager =getFragmentManager();

                if (fragmentManager.getBackStackEntryCount() > 0) {
                    FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
                    fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.detailFrameLayout, messageFragment).addToBackStack(null).commit();

            }
        });

        ForecastQuery forecastQuery = new ForecastQuery();
        forecastQuery.execute();
    }

    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String minTemp;
        private String maxTemp;
        private String currentTemp;
        private Bitmap picture;
        String iconName;
        int totalMinutes=0;

        @Override
        protected String doInBackground(String... params) {
;
            try {
                this.publishProgress(25);
                TimeUnit.SECONDS.sleep(1);

                Cursor c1 = db.rawQuery("select * from " + TABLE_NAME,null);
                this.publishProgress(50);
                TimeUnit.SECONDS.sleep(1);
                c1.moveToFirst();
                while(!c1.isAfterLast()) {
                    Log.i(ACTIVITY_NAME, "SQL MESSAGE " + c1.getString(c1.getColumnIndex(dbHelper.KEY_TYPE)));
                    totalMinutes+=c1.getInt(c1.getColumnIndex(dbHelper.KEY_TIME));
                    c1.moveToNext();
                }


                this.publishProgress(75);
                TimeUnit.SECONDS.sleep(1);

                this.publishProgress(100);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            totalTime = (TextView) findViewById(R.id.totalTime);
            totalTime.setText("Total activites time is\n" + totalMinutes + "minutes");
        }
    }

    private class ActivityTrackingAdapter extends ArrayAdapter<String> {
        public ActivityTrackingAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return messages.size();
        }

        public String getItem (int position) {
            return messages.get(position).getType();
        }

        public Activites getActivity (int position) {
            return messages.get(position);
        }

        public View getView (int position, View converView, ViewGroup parent){

            LayoutInflater inflater = ActivityTracking.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.activity_tracking_listview, null);

            TextView message = (TextView)result.findViewById(R.id.textView);
            message.setText( getItem(position)); // get the string at position
            return result;

        }
    }

    private class Activites {
        String type;
        int minutes;
        String comments;
        Date currentTime;

        public Activites(String t, int m, String c){
            type=t;
            minutes=m;
            comments=c;
            currentTime = Calendar.getInstance().getTime();
        }

        public String getType(){
            return  type;
        }

        public int getMinutes(){
            return minutes;
        }

        public String getComments(){
            return comments;
        }

        public Date getTime(){
            return currentTime;
        }

        public String toString(){
            String details= "Activity: " + type
                    + ", spent " + minutes + " minutes."
                    + " \nComments: " + comments
                    + " \nRecord time: " + currentTime;
            return details;
        }
    }
}
