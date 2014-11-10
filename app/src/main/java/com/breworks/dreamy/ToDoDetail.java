package com.breworks.dreamy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.PendingIntent.getActivity;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;

/**
 * Created by arsianindita on 27-Oct-14.
 */
<<<<<<< HEAD
public class ToDoDetail extends Activity{

    DatePicker datePicker;
    TimePicker timePicker;
    int day;
    int month;
    int year;
    int hour;
    int minute;
    Calendar calendar;

    EditText dateField;
    EditText timeField;
=======
public class ToDoDetail extends DreamyActivity {
>>>>>>> ui-design

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tododetails_form);
        dateField = (EditText) findViewById(R.id.dateField);
        timeField = (EditText) findViewById(R.id.timeField);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        updateDate();
        updateTime();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.YEAR, year);
                updateDate();
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
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateTime();
            }
        };


        timeField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v1) {
                new TimePickerDialog(ToDoDetail.this, time, hour, minute, false).show();
            }
        });;



    }

    public void updateDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateField.setText(dateFormat.format(calendar.getTime()));
    }

    public void updateTime(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm ");
        timeField.setText(timeFormat.format(calendar.getTime()));
    }
    



}

