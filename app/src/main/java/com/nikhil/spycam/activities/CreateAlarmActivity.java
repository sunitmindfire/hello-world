package com.nikhil.spycam.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nikhil.spycam.R;
import com.nikhil.spycam.constants.SpyCamConstants;
import com.nikhil.spycam.database.SpyCamDBHelper;
import com.nikhil.spycam.enums.ServiceType;
import com.nikhil.spycam.services.BackGroundVideoRecorderService;
import com.nikhil.spycam.utils.AlarmManagerUtility;
import com.nikhil.spycam.utils.DialogUtility;
import com.nikhil.spycam.utils.SpyCamUtility;
import com.nikhil.spycam.utils.logUtility.MyLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateAlarmActivity extends AppCompatActivity {

    private TextView mTimeTextView;
    private TextView mDateTextView;
    private TextView mDurationTextView;
    private int mDuration = 5;
    private final Calendar mCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        initializeViews();
        setViewData();
    }

    private void setViewData() {
        //set Current Date and Time
        Calendar calendar = Calendar.getInstance();
        setTimeTextView(calendar);
        setDateTextView(calendar);
    }

    private void setDateTextView(Calendar calendar) {
        mDateTextView.setText(new SimpleDateFormat("d-MMM-yyyy", Locale.US).format(calendar.getTime()));
    }

    private void setTimeTextView(Calendar calendar) {
        mTimeTextView.setText(new SimpleDateFormat("h:mm a", Locale.US).format(calendar.getTime()));
    }

    private void initializeViews() {
        mTimeTextView = (TextView) findViewById(R.id.time_textView);
        mDateTextView = (TextView) findViewById(R.id.dateTextView);
        mDurationTextView = (TextView) findViewById(R.id.durationTextView);
    }

    public void showTimePicker(View view) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();

        // Launch Time Picker Dialog
        new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        MyLog.e("Alarm", "hour:" + hourOfDay + " minute" + minute);
                        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mCalendar.set(Calendar.MINUTE, minute);
                        setTimeTextView(mCalendar);
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
    }

    public void showDatePicker(View view) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mCalendar.set(year, monthOfYear, dayOfMonth);
                        setDateTextView(mCalendar);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void showDurationBar(View view) {
        View progressView = getLayoutInflater().inflate(R.layout.custom_progress_dialog,null);
        final TextView minTextView = (TextView)progressView.findViewById(R.id.min_textView);
        SeekBar progressBar = (SeekBar) progressView.findViewById(R.id.custom_progress_bar);
        progressBar.setMax(58);
        progressBar.setProgress(0);
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDuration = progress + 2;
                minTextView.setText(mDuration + " min");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        DialogUtility.showDialog(this, R.string.duration_dialog_message, R.string.ok, R.string.cancel, progressView, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case Dialog.BUTTON_POSITIVE:
                        mDurationTextView.setText(minTextView.getText());
                }
            }
        });

    }

    public void setAlarm(View view) {
        int uniqueID = (int)mCalendar.getTimeInMillis();

        SpyCamDBHelper.addAlarm(this,mDateTextView.getText().toString(),mTimeTextView.getText().toString(),mDuration,uniqueID);

        Bundle bundle = new Bundle();
        bundle.putSerializable(SpyCamConstants.KEY_SERVICE_TYPE, ServiceType.ALARM);
        bundle.putInt(SpyCamConstants.KEY_DURATION,mDuration);

        Intent intent = new Intent(this, BackGroundVideoRecorderService.class);
        intent.putExtra(SpyCamConstants.KEY_RECORDER_SERVICE,bundle);

        PendingIntent pendingIntent = PendingIntent.getService(this,uniqueID, intent, 0);

        AlarmManager alarmManager = AlarmManagerUtility.getAlarmManager(this);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
        }

        Snackbar.make(findViewById(R.id.coordinatorLayout), "Alarm set", Snackbar.LENGTH_SHORT).show();
        finish();
    }
}
