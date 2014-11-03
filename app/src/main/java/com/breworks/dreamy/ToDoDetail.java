package com.breworks.dreamy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
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
    int taskID;

    EditText dateField;
    EditText timeField;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tododetails_form);
        dateField = (EditText) findViewById(R.id.dateField);
        timeField = (EditText) findViewById(R.id.timeField);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm ");

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

       dateField.setText(dateFormat.format(calendar.getTime()));
       timeField.setText(timeFormat.format(calendar.getTime()));
    }

    public void setTaskID(int taskID){
        this.taskID = taskID;
    }



}
