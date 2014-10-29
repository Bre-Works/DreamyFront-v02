package com.breworks.dreamy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import static android.app.PendingIntent.getActivity;

/**
 * Created by arsianindita on 27-Oct-14.
 */
public class ToDoDetail extends Activity{

    DatePicker datePicker;
    TimePicker timePicker;
    int day;
    int month;
    int year;
    Calendar calendar;
    Button setDate;
    Button setTime;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tododetails_form);
        setDate = (Button) findViewById(R.id.setDate);
        setTime = (Button) findViewById(R.id.setTime);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

    }



}
