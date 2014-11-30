package com.breworks.dreamy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.Milestone;
import com.breworks.dreamy.model.Todo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by aidifauzan on 23/11/2014.
 */
public class SelectedMiles extends Activity {

    SessionManager session;
    int selectedDreamIndex = 0;
    int selectedMilesIndex = 0;
    EditText TodoInput;
    CheckBox TodoCheck;
    LinearLayout container;
    ImageButton toDetail;
    RelativeLayout llayout;

    String[] colorsample = {"#FFFFFFFF","#FFE8FAFF","#FFEDFF6E","#FFEAFFE1"};

    long selectedDream = 0;
    long selectedMiles = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectedmiles);

        //get data from intent
        Intent intent = getIntent();
        int color = intent.getIntExtra("color",0);
        long dream = intent.getLongExtra("dream", 0);
        long milest = intent.getLongExtra("miles", 0);
        selectedDream = dream;
        selectedMiles = milest;

        Log.e("dream ID!!", String.valueOf(dream));

        final Dream dr = Dream.findById(Dream.class, dream);
        Milestone m = Milestone.findById(Milestone.class, milest);

        TextView tv1 = (TextView)findViewById(R.id.dreamtv);
        tv1.setText(dr.getName().toString());
        TextView tv2 = (TextView)findViewById(R.id.milestv);
        tv2.setText(m.getName().toString());
        llayout = (RelativeLayout)findViewById(R.id.layoutbox);
        llayout.setBackgroundColor(Color.parseColor(colorsample[color]));

        TodoInput = (EditText) findViewById(R.id.Inputted);
        container = (LinearLayout) findViewById(R.id.container);

        ToDoSetUp(m);

        TodoInput.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService
                                    (Context.LAYOUT_INFLATER_SERVICE);

                            final View addView = inflater.inflate(R.layout.todo_row, null);

                            toDetail = (ImageButton) addView.findViewById(R.id.toDetail);

                            CheckBox todoc = (CheckBox) addView.findViewById(R.id.cbTodo);

                            final EditText editText = (EditText) addView.findViewById(R.id.taskInput);

                            editText.setText(TodoInput.getText().toString());

                            String toDoInput = TodoInput.getText().toString();

                            Milestone selectedMilestone = Milestone.findById(Milestone.class, selectedMiles);

                            String miles = selectedMilestone.getId().toString();

                            Log.e("current Milestone", miles);

                            Calendar calendar = Calendar.getInstance();
                            Date currentDate = calendar.getTime();
                            final Todo todo = Todo.createTodo(toDoInput, false, selectedMilestone, currentDate);

                            todoc.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    if (((CheckBox) v).isChecked()) {
                                        Log.e("lol", "wooooi");
                                        todo.setStatus(true);
                                        editText.setPaintFlags(editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    } else {
                                        todo.setStatus(false);
                                        editText.setPaintFlags(editText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                                    }
                                }
                            });

                            Log.e("Todo", todo.getText());

                            toDetail.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v1) {
                                    Intent intent = new Intent(SelectedMiles.this, ToDoDetail.class);
                                    intent.putExtra("taskID", todo.getId());
                                    startActivity(intent);
                                }
                            });

                            container.addView(addView);
                            TodoInput.setText("");

                            return true;

                    }
                }
                return false;
            }
        });


        //keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void ToDoSetUp(Milestone mil) {


        List<Todo> Todos = Todo.searchByMilestone(mil);
        for (final Todo td : Todos) {

            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            final View addView = inflater.inflate(R.layout.todo_row, null);

            toDetail = (ImageButton) addView.findViewById(R.id.toDetail);

            CheckBox todoc = (CheckBox) addView.findViewById(R.id.cbTodo);

            final EditText editText = (EditText) addView.findViewById(R.id.taskInput);

            editText.setText(td.getText().toString());

            if (td.getStatus()) {
                todoc.setChecked(true);
                editText.setPaintFlags(editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            todoc.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        Log.e("lol", "wooooi");
                        td.setStatus(true);
                        editText.setPaintFlags(editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        td.setStatus(false);
                        editText.setPaintFlags(editText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                }
            });

            toDetail.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v1) {
                    Intent intent = new Intent(SelectedMiles.this, ToDoDetail.class);
                    intent.putExtra("taskID", td.getId());
                    startActivity(intent);
                }
            });

            container.addView(addView);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TodoSquare.class);
        intent.putExtra("key", selectedDream);
        startActivity(intent);
        finish();
    }

}
