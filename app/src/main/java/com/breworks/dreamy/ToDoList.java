package com.breworks.dreamy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.inputmethod.EditorInfo;

import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.Milestone;
import com.breworks.dreamy.model.Todo;
import com.breworks.dreamy.tabpanel.MyTabHostProvider;
import com.breworks.dreamy.tabpanel.TabHostProvider;
import com.breworks.dreamy.tabpanel.TabView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.orm.SugarRecord;

/**
 * Created by Maha on 9/28/14.
 */

public class ToDoList extends Activity {

    ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    ArrayList<EditText> textFields = new ArrayList<EditText>();
    ArrayList<Integer> dreamIDs = new ArrayList<Integer>();

    Button btnClear;
    TableLayout table;
    OnEditorActionListener taskEnter;
    Display display;
    Point screenSize;
    int screenWidth;
    int fieldWidth;
    int selectedDreamIndex = 0;
    int selectedMilesIndex = 0;
    private boolean clicked;
    private int index = 2;
    private int displayTaskIndex = 1;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabHostProvider tabProvider = new MyTabHostProvider(ToDoList.this);
        TabView tabView = tabProvider.getTabHost("Todo");
        tabView.setCurrentView(R.layout.todolist);
        setContentView(tabView.render(1));

        btnClear = (Button) findViewById(R.id.btnClear);
        table = (TableLayout) findViewById(R.id.tableTaskList);

        // Scaling
        display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);
        screenWidth = screenSize.x;
        fieldWidth = (int) (screenWidth * 0.7);

        taskEnter = new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getText().toString().trim().length() > 0 && actionId == EditorInfo.IME_NULL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    createNewTaskField();
                    return true;
                } else if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    return true;
                }
                return false;
            }
        };

        View.OnClickListener oclBtnClear = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CheckBox> newCB = new ArrayList<CheckBox>();
                ArrayList<EditText> newTF = new ArrayList<EditText>();
                for (int i = 0; i < checkBoxes.size(); i++) {
                    CheckBox cb = checkBoxes.get(i);
                    EditText tf = textFields.get(i);
                    if (cb.isChecked()) {
                        View row = (View) cb.getParent();
                        table.removeView(row);
                        //checkBoxes.remove(cb);
                        //textFields.remove(i);
                    } else {
                        newCB.add(cb);
                        newTF.add(tf);
                    }

                }
                checkBoxes = newCB;
                textFields = newTF;
                createInitFieldIfNotExists();
            }
        };

        btnClear.setOnClickListener(oclBtnClear);

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
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Log.e("nothing", "...");
                    }
                }
        );

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
        long[] milesId = new long[miles.size()];
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
                R.layout.todolist_dream, R.id.dreamArray, milesList);
        SpinnerMiles.setAdapter(adapter2);

        // spinner miles listener
        SpinnerMiles.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.e("choose miles index ", String.valueOf(i));
                        selectedMilesIndex = i;
                        Log.e("save miles index ", String.valueOf(selectedMilesIndex));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Log.e("nothing", "selected");
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Initial task filling
        fetchTasks();
        createInitFieldIfNotExists();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveTasks();
    }

    /*
    Creates a new empty task text field
     */
    protected void createNewTaskField() {

        TableRow row = new TableRow(this);
        EditText textField = new EditText(this);
        CheckBox checkbox = new CheckBox(this);
        Button button = new Button(this);

        detailButtonListener(button, index);

        checkbox.setLayoutParams(new TableRow.LayoutParams(1));
        setEditTextAttributes(textField);
        button.setLayoutParams(new TableRow.LayoutParams(3));
        checkBoxes.add(checkbox);
        textFields.add(textField);

        row.addView(checkbox);
        row.addView(textField);
        row.addView(button);
        table.addView(row);

        index++;
    }

    /*
    Displays a new task field linked to an existing Todo object
     */
    protected void displayTask(Todo task) {

        TableRow row = new TableRow(this);
        EditText textField = new EditText(this);
        CheckBox checkbox = new CheckBox(this);
        Button button = new Button(this);

        detailButtonListener(button, displayTaskIndex);
        if (clicked == true) {
            return;
        }

        checkbox.setLayoutParams(new TableRow.LayoutParams(1));
        checkbox.setChecked(task.getStatus());
        setEditTextAttributes(textField);
        button.setLayoutParams(new TableRow.LayoutParams(3));

        checkBoxes.add(checkbox);
        textFields.add(textField);

        CharSequence text = task.getText();

        textField.setText(text);

        row.addView(checkbox);
        row.addView(textField);
        row.addView(button);
        table.addView(row);

        displayTaskIndex++;
    }

    protected void setEditTextAttributes(EditText et) {
        et.setOnEditorActionListener(taskEnter);
        et.requestFocus();
        et.setLayoutParams(new TableRow.LayoutParams(2));
        et.getLayoutParams().width = fieldWidth;
    }

    /*
    Creates a new initial text field if the task list is empty
     */
    protected void createInitFieldIfNotExists() {
        if (table.getChildCount() == 0) {

            TableRow row = new TableRow(this);
            EditText textField = new EditText(this);
            CheckBox checkbox = new CheckBox(this);
            Button button = new Button(this);

            detailButtonListener(button, 1);

            checkbox.setLayoutParams(new TableRow.LayoutParams(1));

            textField.setOnEditorActionListener(taskEnter);
            textField.setLayoutParams(new TableRow.LayoutParams(2));
            textField.getLayoutParams().width = fieldWidth;
            textField.setHint("add a task");

            button.setLayoutParams(new TableRow.LayoutParams(3));

            checkBoxes.add(checkbox);
            textFields.add(textField);

            row.addView(checkbox);
            row.addView(textField);
            row.addView(button);

            table.addView(row);
        }
    }

    protected void fetchTasks() {
        List<Todo> todos = Todo.listAll(Todo.class);
        for (Todo todo : todos) {
            displayTask(todo);
        }
        //SugarRecord.deleteAll(Todo.class);
    }

    protected void saveTasks() {
        SugarRecord.deleteAll(Todo.class);
        for (int i = 0; i < textFields.size(); i++) {
            CheckBox cb = checkBoxes.get(i);
            EditText tf = textFields.get(i);
            Todo newTask = new Todo(tf.getText().toString(), cb.isChecked());
            //newTask.save();
        }
        textFields.clear();
    }

    public void gotoToDoDetail() {
        Intent intent = new Intent(this, ToDoDetail.class);
        startActivity(intent);
    }

    public void setCurrentID(int id){
        this.id = id;
    }

    public int getCurrentID(){ return id;}


    public void detailButtonListener(Button button, final int taskID) {
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v1) {
                setCurrentID(taskID);
                Log.e("id", String.valueOf(id));
                gotoToDoDetail();
                clicked = true;
            }
        });
    }

}