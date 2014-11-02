package com.breworks.dreamy;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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
    ArrayList<Button> buttons = new ArrayList<Button>();
    Button btnClear;
    TableLayout table;
    OnEditorActionListener taskEnter;
    Display display;
    Point screenSize;
    int screenWidth;
    int fieldWidth;

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
                ArrayList<Button> newButton = new ArrayList<Button>();
                for (int i = 0; i < checkBoxes.size(); i++) {
                    CheckBox cb = checkBoxes.get(i);
                    EditText tf = textFields.get(i);
                    Button b = buttons.get(i);
                    if (cb.isChecked()) {
                        View row = (View) cb.getParent();
                        table.removeView(row);
                        //checkBoxes.remove(cb);
                        //textFields.remove(i);
                    } else {
                        newCB.add(cb);
                        newTF.add(tf);
                        newButton.add(b);

                    }
                }
                checkBoxes = newCB;
                textFields = newTF;
                buttons = newButton;
                createInitFieldIfNotExists();
            }
        };

        btnClear.setOnClickListener(oclBtnClear);

        // get dream from database
        List<Dream> dreams = Dream.listAll(Dream.class);
        String[] dreamList = new String[dreams.size()];
        for (int i=0; i < dreams.size(); i++) {
            dreamList[i] = dreams.get(i).toString();
        }

        // spinner
        Spinner mySpinner = (Spinner)findViewById(R.id.DreamSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.todolist_dream, R.id.dreamArray, dreamList);
        mySpinner.setAdapter(adapter);

        //keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

        checkbox.setLayoutParams(new TableRow.LayoutParams(1));
        setEditTextAttributes(textField);
        button.setLayoutParams(new TableRow.LayoutParams(3));
        checkBoxes.add(checkbox);
        textFields.add(textField);
        buttons.add(button);
        row.addView(checkbox);
        row.addView(textField);
        row.addView(button);
        table.addView(row);
    }

    /*
    Displays a new task field linked to an existing Todo object
     */
    protected void displayTask(Todo task) {
        TableRow row = new TableRow(this);
        EditText textField = new EditText(this);
        CheckBox checkbox = new CheckBox(this);
        Button button = new Button(this);

        checkbox.setLayoutParams(new TableRow.LayoutParams(1));
        checkbox.setChecked(task.getStatus());
        setEditTextAttributes(textField);
        button.setLayoutParams(new TableRow.LayoutParams(2));

        checkBoxes.add(checkbox);
        textFields.add(textField);
        buttons.add(button);

        CharSequence text = task.getText();
        textField.setText(text);
        row.addView(checkbox);
        row.addView(textField);
        row.addView(button);
        table.addView(row);
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
    protected void createInitFieldIfNotExists(){
        if (table.getChildCount() == 0) {
            TableRow row = new TableRow(this);
            EditText textField = new EditText(this);
            CheckBox checkbox = new CheckBox(this);
            Button button = new Button(this);

            checkbox.setLayoutParams(new TableRow.LayoutParams(1));

            textField.setOnEditorActionListener(taskEnter);
            textField.setLayoutParams(new TableRow.LayoutParams(2));
            textField.getLayoutParams().width = fieldWidth;
            textField.setHint("add a task");

            checkbox.setLayoutParams(new TableRow.LayoutParams(3));

            checkBoxes.add(checkbox);
            textFields.add(textField);
            buttons.add(button);

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
        SugarRecord.deleteAll(Todo.class);
    }

    protected void saveTasks() {
        //SugarRecord.deleteAll(Todo.class);
        for (int i = 0; i < textFields.size(); i++) {
            CheckBox cb = checkBoxes.get(i);
            EditText tf = textFields.get(i);
            Todo newTask = new Todo(tf.getText().toString(), cb.isChecked());
            //newTask.save();
        }
        textFields.clear();
    }

}