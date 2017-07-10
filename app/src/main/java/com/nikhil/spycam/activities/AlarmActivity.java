package com.nikhil.spycam.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.nikhil.spycam.R;
import com.nikhil.spycam.adapters.AlarmAdapter;
import com.nikhil.spycam.database.SpyCamDBHelper;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //disable Action Bar default Title
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        //set Text of Toolbar Tittle Text
        ((TextView) findViewById(R.id.toolbar_textView)).setText(R.string.alarm);

        initializeViews();

    }

    private void initializeViews() {
        ListView listView = (ListView)findViewById(R.id.alarmList);
        listView.setAdapter(new AlarmAdapter(this, SpyCamDBHelper.getAlarmList(getApplicationContext()),getLayoutInflater()));

    }

    /**
     * take user to create new alarm screen
     * @param view
     */
    public void handleFloatingButtonAction(View view) {
        startActivity(new Intent(this,CreateAlarmActivity.class));
    }
}
