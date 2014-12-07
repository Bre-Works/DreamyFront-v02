package com.breworks.dreamy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.PendingIntent.getActivity;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.Todo;

/**
 * Created by arsianindita on 27-Oct-14.
 */
public class ToDoDetail extends DreamyActivity {
    int day;
    int month;
    int year;
    int hour;
    int minute;
    Calendar calendar;
    Date taskDeadline;
    Date theDeadline;
    long taskID;

    EditText dateField;
    EditText timeField;
    TextView taskText;
    Switch notificationSwitch;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tododetails_form);
        dateField = (EditText) findViewById(R.id.dateField);
        timeField = (EditText) findViewById(R.id.timeField);
        taskText = (TextView) findViewById(R.id.taskText);
        notificationSwitch = (Switch) findViewById(R.id.notification);


        Intent intent = getIntent();
        taskID = intent.getLongExtra("taskID", 0);
        Log.e("tududud ID!!", String.valueOf(taskID));

        final Todo t = Todo.findByTaskID(taskID);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);


        taskText.setText(t.getTask(t));
        theDeadline = t.getDeadline(t);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateField.setText(dateFormat.format(theDeadline));
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm ");
        timeField.setText(timeFormat.format(theDeadline));


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.YEAR, year);
                taskDeadline = calendar.getTime();
                updateDeadline(t);
            }
        };

        dateField.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                new DatePickerDialog(ToDoDetail.this, date, year, month, day).show();
            }
        });;


        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {


            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                taskDeadline = calendar.getTime();
                updateDeadline(t);
            }
        };


        timeField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v1) {
                new TimePickerDialog(ToDoDetail.this, time, hour, minute, false).show();
            }
        });;

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Alarm a = new Alarm();
                // If the notification is turned on
                if (isChecked) {
                    Log.e("The switch is on!", String.valueOf(isChecked));
                    a.setDeadline(t.getDeadline(t));
                    a.setAlarm(getApplicationContext());
                } else{
                    Log.e("The switch is off!", String.valueOf(isChecked));
                    if (a!= null) {
                        a.cancelAlarm(getApplicationContext());
                    }
                }
            }
        });



    }

    public void updateDeadline(Todo t){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateField.setText(dateFormat.format(calendar.getTime()));
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm ");
        timeField.setText(timeFormat.format(calendar.getTime()));
        t.saveDeadline(taskDeadline, t);
    }

}

