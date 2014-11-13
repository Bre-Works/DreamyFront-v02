package com.breworks.dreamy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.Milestone;
import com.breworks.dreamy.model.Todo;
import com.breworks.dreamy.tabpanel.MyTabHostProvider;
import com.breworks.dreamy.tabpanel.TabHostProvider;
import com.breworks.dreamy.tabpanel.TabView;

import java.util.List;

/**
 * Created by Ryan Avila on 12/11/2014.
 */
public class ToDoListNew extends Activity {

    int selectedDreamIndex = 0;
    int selectedMilesIndex = 0;
    EditText TodoInput;
    CheckBox TodoCheck;
    LinearLayout container;
    ImageButton removeTodo;

    long selectedMiles = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        TabHostProvider tabProvider = new MyTabHostProvider(ToDoListNew.this);
        TabView tabView = tabProvider.getTabHost("Todo");
        tabView.setCurrentView(R.layout.todolist_new);
        setContentView(tabView.render(1));

        // DREAMS
        // get dream from database
        List<Dream> dreams = Dream.listAll(Dream.class);
        final long[] dreamId = new long[dreams.size()];
        final String[] dreamList = new String[dreams.size()];
        int inc = 0;

        for (final Dream dr : dreams) {
            Log.e("dream id", String.valueOf(dr.getId()));
            dreamId[inc] = dr.getId();
            dreamList[inc] = dr.getName();
            inc++;
        }

        // spinner dream
        Spinner SpinnerDream = (Spinner) findViewById(R.id.DreamSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.todolist_dream, R.id.dreamArray, dreamList);
        SpinnerDream.setAdapter(adapter);

        // spinner dream listener
        SpinnerDream.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.e("Choose dream index ", String.valueOf(i));
                        selectedDreamIndex = i;
                        Log.e("index saved ", String.valueOf(selectedDreamIndex));
                        checkDreamIndex(dreamList, dreamId);
                        container.removeAllViewsInLayout();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Log.e("nothing", "...");
                    }
                }
        );
        TodoInput = (EditText) findViewById(R.id.Inputted);
        container = (LinearLayout) findViewById(R.id.container);

        TodoInput.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService
                                (Context.LAYOUT_INFLATER_SERVICE);

                        final View addView = inflater.inflate(R.layout.dreamy_form_row,null);

                        removeTodo = (ImageButton) addView.findViewById(R.id.delMilestone);

                        CheckBox todoc = (CheckBox) addView.findViewById(R.id.milestoneOut);

                        final EditText editText = (EditText) addView.findViewById(R.id.Inputted);

                        editText.setText(TodoInput.getText().toString());

                        String toDoInput = TodoInput.getText().toString();

                        Milestone selectedMilestone = Milestone.findById(Milestone.class, selectedMiles);
                        final Todo todo = Todo.createTodo(toDoInput, false, selectedMilestone);

                        todoc.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (((CheckBox) v).isChecked()) {
                                    Log.e("lol", "wooooi");
                                    todo.setStatus(true);
                                    editText.setPaintFlags(editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                }
                                else {
                                    todo.setStatus(false);
                                    editText.setPaintFlags(editText.getPaintFlags() & ~ Paint.STRIKE_THRU_TEXT_FLAG);
                                }
                            }
                        });

                        Log.e("Todo", todo.getText());

                        removeTodo.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v1) {
                                ((LinearLayout) addView.getParent()).removeView(addView);
                                todo.delete();
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

    public void checkDreamIndex(String[] dreamList, long[] dreamId) {
        if (dreamList[selectedDreamIndex] != null) {
            Log.e("index ", "amazingly not null");
            Dream dr = Dream.findById(Dream.class, dreamId[selectedDreamIndex]);// ALERT!!
            milestonesSetUp(dr);
        } else {
            Log.e("index ", "is incredibly null");
            selectedDreamIndex = 0;
            Dream dr = Dream.findById(Dream.class, dreamId[selectedDreamIndex]);
            milestonesSetUp(dr);
        }
    }

    public void milestonesSetUp(Dream dr) {
        // MILESTONES
        // get miles from database
        Log.e("arrive here ", "LOH");
        List<Milestone> miles = Milestone.searchByDream(dr);
        final long[] milesId = new long[miles.size()];
        String[] milesList = new String[miles.size()];
        int incM = 0;
        for (Milestone mil : miles) {
            Log.e("miles id", String.valueOf(mil.getId()));
            milesId[incM] = mil.getId();
            milesList[incM] = mil.getName();
            incM++;
        }
        // spinner miles
        Spinner SpinnerMiles = (Spinner) findViewById(R.id.MilestoneSpinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.todolist_miles, R.id.milesArray, milesList);
        SpinnerMiles.setAdapter(adapter2);

        // spinner miles listener
        SpinnerMiles.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.e("choose miles index ", String.valueOf(i));
                        selectedMilesIndex = i;
                        selectedMiles = milesId[selectedMilesIndex];
                        Milestone m = Milestone.findById(Milestone.class,milesId[selectedMilesIndex]);
                        Log.e("save miles index ", String.valueOf(selectedMilesIndex));
                        container.removeAllViewsInLayout();
                        ToDoSetUp(m);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Log.e("nothing", "selected");
                    }
                }
        );
    }

    public void ToDoSetUp(Milestone mil){

        List<Todo>Todos = Todo.searchByMilestone(mil);
        for(final Todo td : Todos){
            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            final View addView = inflater.inflate(R.layout.dreamy_form_row,null);

            removeTodo = (ImageButton) addView.findViewById(R.id.delMilestone);

            CheckBox todoc = (CheckBox) addView.findViewById(R.id.milestoneOut);

            final EditText editText = (EditText) addView.findViewById(R.id.Inputted);

            editText.setText(td.getText().toString());

            if(td.getStatus()){
                todoc.setChecked(true);
                editText.setPaintFlags(editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            todoc.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        Log.e("lol", "wooooi");
                        td.setStatus(true);
                        editText.setPaintFlags(editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    else {
                        td.setStatus(false);
                        editText.setPaintFlags(editText.getPaintFlags() & ~ Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                }
            });

            removeTodo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v1) {
                    ((LinearLayout) addView.getParent()).removeView(addView);
                    td.delete();
                }
            });

            container.addView(addView);
        }
    }


}
