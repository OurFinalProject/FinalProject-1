package com.example.a29751.finalproject;
/**
 * Date: 2017-10-31
 * Finished by Cheng Yan
 * This is about House Thermostat
 */
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.design.widget.Snackbar;
import java.util.ArrayList;
import java.util.List;

import java.util.Calendar;

public class HouseThermostat extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemSelectedListener {

    private ListView listView;
    private EditText eTextWeek, eTextTemp;
    private TextView eTextTime;
    private Button buttonAdd,buttonSetTime;
    HouseThermostatDatabaseHelper houseThermostatDatabaseHelper;
    HouseThermostatAdapter messageAdapterHT;
    Cursor cursor;
    ArrayList<String[]> chatMessageArr = new ArrayList<>();
    ArrayList<String> chatMessage = new ArrayList<>();
    protected static final String ACTIVITY_NAME = "HouseThermostat";
    SQLiteDatabase db;
    HouseThermostatMessageFragment houseThermostatMessageFragment;
    Boolean fb1;
    private ProgressBar pBar;
    String responseText = "1.0";
    String addText = null;
    String dayOfWeek;

//    String dayOfWeek = "Monday";
    DrawerLayout drawer;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Spinner spinner1, spinner2;

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        dayOfWeek = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_thermostat);
        addListenerOnSpinnerItemSelection();
     //   responseText = "1.0";
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_string, R.string.close_string);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        Log.i(ACTIVITY_NAME, "In onCreate()");
        listView = (ListView)findViewById(R.id.listView_HouseThermostat);
        eTextWeek   = (EditText) findViewById(R.id.edittext_Week_HouseThermostat);
        //eTextTime   = (EditText) findViewById(R.id.edittext_Time_HouseThermostat);
        eTextTime   = (TextView) findViewById(R.id.edittext_Time_HouseThermostat);

        eTextTemp   = (EditText) findViewById(R.id.edittext_Temp_HouseThermostat);
        //listView.setAdapter();
        messageAdapterHT =new HouseThermostatAdapter(this);
        listView.setAdapter(messageAdapterHT);
        fb1 = findViewById(R.id.frameLayout_houseThermostat) != null;//layout-sw600dp/activity_chat_window.xml

        pBar = (ProgressBar)findViewById(R.id.progress_bar);
        pBar.setVisibility(View.VISIBLE);
        pBar.setProgress(0);

        houseThermostatDatabaseHelper = new HouseThermostatDatabaseHelper(this);
        db = houseThermostatDatabaseHelper.getWritableDatabase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long id) {
                String messa=(String)adapter.getItemAtPosition(position);
              //  displayProgressBar();
                String weekS = messageAdapterHT.getItemArr(position)[0];
                String timeS = messageAdapterHT.getItemArr(position)[1];
                String tempS = messageAdapterHT.getItemArr(position)[2] +messageAdapterHT.getItemArr(position)[3];

                long mId  = messageAdapterHT.getItemId(position);
                String messageId =String.valueOf( mId);
                Log.d("wen",messageId);

                //Tablet
                if(fb1||getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("message", messa );
                    bundle.putLong("mId",mId);
                    bundle.putString("mweek",weekS);
                    bundle.putString("mtime",timeS);
                    bundle.putString("mtemp",tempS);
                    bundle.putString("messageId", messageId );
                    houseThermostatMessageFragment = HouseThermostatMessageFragment.newInstance(HouseThermostat.this);//chatWindow not null, on tablet
                    houseThermostatMessageFragment.setArguments(bundle);//Supply the construction arguments for this fragment.

                    getFragmentManager().beginTransaction().replace(R.id.frameLayout_houseThermostat, houseThermostatMessageFragment).commit();//in layout-sw600dp//activity_chat_windows.xml
                } else {
                    Intent intent = new Intent(HouseThermostat.this, HouseThermostatMessageDetails.class);
                    intent.putExtra("message", messa);
                    intent.putExtra("messageId", messageId);
                    intent.putExtra("mweek", weekS);
                    intent.putExtra("mtime", timeS);
                    intent.putExtra("mtemp", tempS);
                    startActivityForResult(intent, 10);

                }
            }
        });

        cursor = db.rawQuery("select * from HouseThermostatInfo", null);

        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(HouseThermostatDatabaseHelper.WEEK_MESSAGE)));

                String newStringWeek1 = cursor.getString(cursor.getColumnIndex(HouseThermostatDatabaseHelper.WEEK_MESSAGE));
                String newStringTime1 = cursor.getString(cursor.getColumnIndex(HouseThermostatDatabaseHelper.TIME_MESSAGE));
                String newStringTemp1 = cursor.getString(cursor.getColumnIndex(HouseThermostatDatabaseHelper.TEMP_MESSAGE));
                String [] newStringArr1 = new String []{newStringWeek1, newStringTime1, " Temp -> ", newStringTemp1};

                chatMessageArr.add(newStringArr1);
                chatMessage.add(newStringWeek1+newStringTime1+" Temp -> "+newStringTemp1);
                //           chatMessage.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }

        Log.i(ACTIVITY_NAME, "Cursor's  column count =" + cursor.getColumnCount() );

        for(int i = 0; i < cursor.getColumnCount(); i++){
            System.out.println(cursor.getColumnName(i));
        }

        buttonSetTime = (Button)findViewById(R.id.timeButton_HouseThermostat);
        buttonSetTime.setOnClickListener(new View.OnClickListener() {
                                             public void onClick(View v) {
                                                 final Calendar c = Calendar.getInstance();
                                                 mHour = c.get(Calendar.HOUR_OF_DAY);
                                                 mMinute = c.get(Calendar.MINUTE);
                                                 // Launch Time Picker Dialog
                                                 TimePickerDialog timePickerDialog =  new TimePickerDialog(HouseThermostat.this,
                                                         new TimePickerDialog.OnTimeSetListener(){
                                                             @Override
                                                             public void onTimeSet(TimePicker view, int hourOfDay,
                                                                                   int minute) {
                                                                 eTextTime.setText(hourOfDay + ":" + minute);
                                                             }
                                                         }, mHour, mMinute, true);
                                                 timePickerDialog.show();

                                            }




                                         });


        buttonAdd = (Button)findViewById(R.id.addButton_HouseThermostat);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addText = "";

                //String newStringWeek = eTextWeek.getText().toString();
                String newStringTime = eTextTime.getText().toString();
                String newStringTemp = eTextTemp.getText().toString();

                String newStringWeek = dayOfWeek;
                //String newStringTime = eTextTime.getText().toString();
                //String newStringTemp = "22";
                if (newStringWeek.equals(""))
      //              addText += "Week can not be null! ";
                    addText += getString(R.string.HTWeekNull);
                    if (newStringTime.equals("Time"))
                    addText += getString(R.string.HTTimeNull);
                if (newStringTemp.equals(""))
                    addText += getString(R.string.HTTempNull);
                if (addText.equals(""))
                {
//                    addText = "Added successful!";
                    //addText = "";
                    addText = getString(R.string.HTAddSucc);
                String[] newStringArr = new String[]{newStringWeek, newStringTime, " Temp -> ", newStringTemp};
                chatMessageArr.add(newStringArr);
                chatMessage.add(newStringWeek + newStringTime + " Temp -> " + newStringTemp);
                //       chatMessage.asList("everton", "liverpool", "swansea", "chelsea");
                messageAdapterHT.notifyDataSetChanged();
                dayOfWeek = "Monday";
                //eTextTime.setText("");
                eTextTemp.setText("");

                ContentValues cValues = new ContentValues();
                cValues.put("week", newStringWeek);
                cValues.put("time", newStringTime);
                //cValues.put("temp", "Temp-->" + newStringTemp);
                cValues.put("temp", " " + newStringTemp);
                db.insert("HouseThermostatInfo", null, cValues);

                cursor = db.rawQuery("select * from HouseThermostatInfo", null);
                chatMessage.clear();
                chatMessageArr.clear();
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String s = cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE))
                                + cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE))
                                + " Temp -> "
                                + cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE));
                        Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + s);
                        String[] newStringArr1 = new String[]{cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE)),
                                cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE)), " Temp -> ",
                                cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE))};
                        chatMessage.add(s);
                        chatMessageArr.add(newStringArr1);
                        //           chatMessage.add(cursor.getString(1));
                        cursor.moveToNext();
                    }
                }
                messageAdapterHT.notifyDataSetChanged();
            }
            View v3;
   if (fb1)  v3 = (View) findViewById(R.id.land_test_house_thermostat);
   else  v3 = (View) findViewById(R.id.test_house_thermostat);
                Snackbar.make(v3, addText, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private class HouseThermostatAdapter extends ArrayAdapter<String> {
        public HouseThermostatAdapter(Context ctx){
            super(ctx, 0);
        }

        public int getCount(){
            return chatMessage.size();
        }

        public  String getItem(int position){
            return chatMessage.get(position);
        }

        public long getItemId(int position) {
            //      db = chatDbHelper.getWritableDatabase();
            cursor.moveToPosition(position);
            long dbId = 0;
            if (cursor.getCount() > position) {
                dbId = cursor.getLong(0);
            }
            //     Log.d("ww",String.valueOf(dbId));
            return dbId;
        }

        public  String[] getItemArr(int position){
            return chatMessageArr.get(position);

        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = HouseThermostat.this.getLayoutInflater();
            View result = null ;
                result = inflater.inflate(R.layout.housethermostat_row, null);
            TextView houseThermostatMessage = (TextView)result.findViewById(R.id.textview_HouseThermostat);
            houseThermostatMessage.setText(getItem(position)); // get the string at position
            return result;
        }
    }

    public void deleteMsg(int id) {
        db = houseThermostatDatabaseHelper.getWritableDatabase();
        db.delete("HouseThermostatInfo", "id=?", new String[]{String.valueOf(id)});
    }

    public void deleteTabletMsg(int id) {
        deleteMsg(id);
        updateListView();
        getFragmentManager().beginTransaction().remove(houseThermostatMessageFragment).commit();
    }

    public void updateListView(){
        /*
        cursor = db.rawQuery("select * from HouseThermostatInfo", null);
        chatMessage.clear();
        chatMessageArr.clear();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String s = cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE))
                        +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE))
                        +" Temp -> "
                        +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + s);
                chatMessage.add(s);
                String [] newStringArr1 = new String []{cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE)),
                        cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE)), " Temp -> ",
                        cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE))};
                chatMessageArr.add(newStringArr1);
                //           chatMessage.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
        */
        Updatebase updatebase = new Updatebase();
        updatebase.execute();
        /*messageAdapterHT.notifyDataSetChanged();
        listView.invalidate();//Invalidate the whole view. If the view is visible, onDraw(android.graphics.Canvas) will be called at some point in the future.
        listView.refreshDrawableState();*/
    }

    public void saveNewMsg(int id, String weekS, String timeS, String tempS) {
        db = houseThermostatDatabaseHelper.getWritableDatabase();

        ContentValues cValues1 = new ContentValues();
        cValues1.put("week", weekS);
        cValues1.put("time", timeS);
        String[] parts = tempS.split(">");
        tempS  = parts[1];
        cValues1.put("temp", tempS);
        db.insert("HouseThermostatInfo", null, cValues1);
    }

    public void displayProgressBar(){

        pBar.setMax(30);

        final Thread pBarThread = new Thread() {
            @Override
            public void run() {
                try {
                    int progress =1;
                    while(progress<=30) {
                        pBar.setProgress(progress);
                        sleep(300);
                        ++progress;
                    }
                }
                catch(InterruptedException e) {
                }
            }
        };

        pBarThread.start();

    }

    public void saveNewTabletMsg(int id, String weekS, String timeS, String tempS) {
        saveNewMsg(id, weekS, timeS, tempS);
        updateListView();
    }


    public void updateMsg(int id, String weekS, String timeS, String tempS) {
        db = houseThermostatDatabaseHelper.getWritableDatabase();

        ContentValues cValues1 = new ContentValues();
        cValues1.put("week", weekS);
        cValues1.put("time", timeS);
        String[] parts = tempS.split(">");
        tempS  = parts[1];
        cValues1.put("temp", tempS);
        db.update("HouseThermostatInfo", cValues1, "id=?", new String[]{String.valueOf(id)});
    }

    public void updateTabletMsg(int id, String weekS, String timeS, String tempS) {
        updateMsg(id, weekS, timeS, tempS);
        updateListView();
    }

    private void HTQuery1(int btnType,int resultCode,String weekS, String timeS, String tempS){
        switch (btnType){
            case 1 :   deleteMsg(resultCode);
                        break;
            case 2 :     saveNewMsg(resultCode,weekS,timeS,tempS);
                        break;
            case 3 :   updateMsg(resultCode, weekS,timeS,tempS);
                       break;
            default:   break;
        }


    }



    private class Updatebase extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            int i = urls.length;
            String result = "uuu";

            cursor = db.rawQuery("select * from HouseThermostatInfo", null);
            chatMessage.clear();
            chatMessageArr.clear();
            int len = cursor.getCount();
            int im = 0;
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String s = cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE))
                            +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE))
                            +" Temp -> "
                            +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE));
                    Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + s);
                    chatMessage.add(s);
                    String [] newStringArr1 = new String []{cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE)),
                            cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE)), " Temp -> ",
                            cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE))};
                    chatMessageArr.add(newStringArr1);
                    //           chatMessage.add(cursor.getString(1));
                    im++;
                    try {
                        Thread.sleep(Math.round(3000/len));
                        if (im>=len)
                            publishProgress(100);
                        else {
                            float ac = (float) im / len;
                            publishProgress(Math.round(ac * 100));
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    cursor.moveToNext();

                }

            }


        return result;
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            pBar.setVisibility(View.VISIBLE);
            pBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pBar.setVisibility(View.INVISIBLE);
            messageAdapterHT.notifyDataSetChanged();
            listView.invalidate();//Invalidate the whole view. If the view is visible, onDraw(android.graphics.Canvas) will be called at some point in the future.
            listView.refreshDrawableState();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBar.setVisibility(View.VISIBLE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10&& data != null && data.getExtras() != null) {   //Come back from Cell Phone delete

            int a = resultCode;
            int btnType  = data.getIntExtra("btnType",-1);
            String weekS = data.getStringExtra("weekS");
            String timeS = data.getStringExtra("timeS");
            String tempS = data.getStringExtra("tempS");

            String [] tmp = new String[5];
            HTQuery1(btnType,resultCode,weekS, timeS, tempS);

            updateListView();

            //HTQuery htQuery = new HTQuery();

            /*
            try {
                String aa =htQuery.execute(String.valueOf(btnType),String.valueOf(resultCode),weekS, timeS, tempS).get();
                while(aa==null){}
                updateListView();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            */

         }
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("Toolbar", "Optivvvvvvon 2 selected");
        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("Toolbar", "Optivvvvvvon3 selected");
        switch (id) {
            case R.id.HTaction_one:
                Log.d("Toolbar", "Option 1 selected");
                Log.d("Toolbar", responseText);

                View v2 = (View)findViewById(R.id.test_house_thermostat);
                Snackbar.make(v2, responseText, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                break;

            case R.id.HTaction_two:
                Log.d("Toolbar", "Option 2 selected");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.HTdialogTitle);

                builder.setPositiveButton(R.string.HTok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.HTcancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

// Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();


                break;

            case R.id.HTaction_three:
                Log.d("Toolbar", "Option 3 selected");
                LayoutInflater li= getLayoutInflater();
                LinearLayout rootTag = (LinearLayout)li.inflate(R.layout.htcustomlayout, null);
                final EditText et = (EditText)rootTag.findViewById(R.id.messagename);
                //            final String sss = et.getText().toString();

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle(R.string.HTcustomDialogTitle);
                LayoutInflater inflater = getLayoutInflater();

                builder2.setPositiveButton(R.string.HTok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //String sss = et.getText().toString();
                        Log.d("Toolbar", "jump");

                        responseText = et.getText().toString();
                        Log.d("Toolbar", responseText);

                    }
                });

                builder2.setView(rootTag);

                builder2.setNegativeButton(R.string.HTcancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

// Create the AlertDialog
                AlertDialog dialog2 = builder2.create();
                dialog2.show();

                break;
            case R.id.HTHelp:
                Toast t = Toast.makeText(this, R.string.HThelpItem, Toast.LENGTH_LONG);
                t.show();
                break;
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.nav_item_one:
                String s2 = getString(R.string.HTinstructionItem1) + responseText + getString(R.string.HTinstructionItem2);
                Toast t1 = Toast.makeText(this, s2, Toast.LENGTH_LONG);
//                Toast t1 = Toast.makeText(this, R.string.HTinstructionItem1 + responseText +R.string.HTinstructionItem1, Toast.LENGTH_LONG);
                t1.show();
                break;
            case R.id.nav_item_two:
                break;
            case R.id.nav_item_three:
                break;
            case R.id.nav_item_four:
                break;
        }
        drawer.closeDrawers();
        return true;
    }

}
