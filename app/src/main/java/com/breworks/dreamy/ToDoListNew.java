package com.breworks.dreamy;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.breworks.dreamy.tabpanel.MyTabHostProvider;
import com.breworks.dreamy.tabpanel.TabHostProvider;
import com.breworks.dreamy.tabpanel.TabView;

import java.util.List;

/**
 * Created by Ryan Avila on 12/11/2014.
 */
public class ToDoListNew extends DreamyActivity{

    int selectedDreamIndex = 0;
    int selectedMilesIndex = 0;
    EditText TodoInput;
    CheckBox TodoCheck;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TabHostProvider tabProvider = new MyTabHostProvider(ToDoListNew.this);
        //TabView tabView = tabProvider.getTabHost("Todo");
        //tabView.setCurrentView(R.layout.todolist_new);
        //setContentView(tabView.render(1));
        getActionBar().setDisplayHomeAsUpEnabled(true);

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
                R.layout.todolist_miles, R.id.milesArray, milesList);
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


}
