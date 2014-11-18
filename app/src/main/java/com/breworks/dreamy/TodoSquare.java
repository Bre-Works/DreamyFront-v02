package com.breworks.dreamy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.breworks.dreamy.SessionManager;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.Milestone;
import com.breworks.dreamy.model.Todo;
import com.breworks.dreamy.model.dreamyAccount;
import com.breworks.dreamy.tabpanel.MyTabHostProvider;
import com.breworks.dreamy.tabpanel.TabHostProvider;
import com.breworks.dreamy.tabpanel.TabView;

import java.util.List;
import java.util.Random;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by aidifauzan on 12/11/2014.
 */
public class TodoSquare extends Activity {

    SessionManager session;
    int selectedDreamIndex = 0;
    int selectedMilesIndex = 0;
    EditText TodoInput;
    CheckBox TodoCheck;
    ImageButton toDetail;
    LinearLayout layout;
    LinearLayout layout2;

    long selectedDreams = 0;
    long selectedMiles = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        TabHostProvider tabProvider = new MyTabHostProvider(TodoSquare.this);
        TabView tabView = tabProvider.getTabHost("Todo");
        tabView.setCurrentView(R.layout.todo_square);
        setContentView(tabView.render(1));
        session = new SessionManager(getApplicationContext());
        dreamyAccount login = session.getUser();

        // DREAMS
        // get dream from database
        List<Dream> dreams = Dream.searchByUser(login);
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
                        layout.removeAllViewsInLayout();
                        layout2.removeAllViewsInLayout();
                        checkDreamIndex(dreamList, dreamId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Log.e("nothing", "...");
                    }
                }
        );

        layout = (LinearLayout) findViewById(R.id.listLayout);
        layout2 = (LinearLayout) findViewById(R.id.listLayout2);

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

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(10, 10, 10, 10);

        List<Milestone> miles = Milestone.searchByDream(dr);
        final long[] milesId = new long[miles.size()];
        String[] milesList = new String[miles.size()];
        int incM = 0;

        for (Milestone mil : miles) {
            Log.e("miles id", String.valueOf(mil.getId()));
            milesId[incM] = mil.getId();
            milesList[incM] = mil.getName();
            String result = mil.getName();
            List<Todo> todos = Todo.searchByMilestone(mil);

            TextView Dc = new TextView(this);

            Drawable rect = getResources().getDrawable(R.drawable.rectangle_border);
            Dc.setBackground(rect);

            int rand = random();
            if(rand == 1){
                ((GradientDrawable)Dc.getBackground()).setColor(Color.parseColor("#FFE8FAFF"));
            }else if(rand == 2){
                ((GradientDrawable)Dc.getBackground()).setColor(Color.parseColor("#FFFFFCEA"));
            }else{
                ((GradientDrawable)Dc.getBackground()).setColor(Color.parseColor("#FFEAFFE1"));
            }

            for(Todo td : todos){
                result = result + "\n" + td.getText().toString();
            }

            //Dc.setPadding(60,10,60,10);
            Dc.setLayoutParams(llp);
            Dc.setText(result);
            Dc.setGravity(Gravity.CENTER);
            Dc.setTypeface(null, Typeface.BOLD_ITALIC);
            //Dc.setTextAppearance(this, android.R.style.TextAppearance_Small);
            if(incM % 2 == 0) {
                layout.addView(Dc);
            }
            else {
                layout2.addView(Dc);
            }
            incM++;
        }
    }

    public int random(){
        Random generator = new Random();
        int i = generator.nextInt(3) + 1;
        return i;
    }

    public void gotoList(View v){
        Intent intent = new Intent(this, ToDoListNew.class);
        startActivity(intent);
        finish();
    }

}
