package com.example.a29751.finalproject;

import android.app.Activity;
import android.os.Bundle;

public class HouseThermostatMessageDetails extends Activity {

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_thermostat_message_details);

        //btDelete = (Button) findViewById(R.id.)
        String message   = getIntent().getStringExtra("message");
        String messageId = getIntent().getStringExtra("messageId");
        String weekS = getIntent().getStringExtra("mweek");
        String timeS = getIntent().getStringExtra("mtime");
        String tempS = getIntent().getStringExtra("mtemp");

        Bundle bundle = new Bundle();
        //String myMessage = "Stackoverflow is cool!";
        bundle.putString("message", message );
        bundle.putString("messageId", messageId );
        bundle.putString("mweek", weekS );
        bundle.putString("mtime", timeS );
        bundle.putString("mtemp", tempS );


        HouseThermostatMessageFragment messageFragment = HouseThermostatMessageFragment.newInstance(null);//if(chatWindow==null) on phone
        //messageFragment.myText1.setText(string);
        //messageFragment.myText2.setText(messageId);
        messageFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.fragment_Container_houseThermostat, messageFragment).commit();//in layout/activity_message_details.xml

    }

}
