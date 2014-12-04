package com.breworks.dreamy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.Milestone;
import com.breworks.dreamy.model.Todo;
import com.breworks.dreamy.model.dreamyAccount;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

/**
 * Created by aidifauzan on 12/11/2014.
 */
public class Milestones extends DreamyActivity {

    public static Activity milestoneAct;
    SessionManager session;
    int selectedDreamIndex = 0;
    LinearLayout layout;
    LinearLayout layout2;

    long selectedDreams = 0;
    String[] colorPalette ={"#FFE1EBFF","#FFFFFAF1","#FFFFF6F8","#FFFBFFF0"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());
        dreamyAccount login = session.getUser();
        setContentView(R.layout.milestones);
        milestoneAct = this;
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Data From Main
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            long dreamInput = intent.getLongExtra("key", 0);
            selectedDreams = dreamInput;
            Log.e("dream ID!!", String.valueOf(dreamInput));
        }

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

        // set main dreams
        int indexOfDream = 0;
        for(int i = 0; i<dreamId.length; i++){
            if(dreamId[i] == selectedDreams){
                indexOfDream = i;
            }
        }
        Log.e("THIS ARRAY INDEX!!", String.valueOf(indexOfDream));
        SpinnerDream.setSelection(indexOfDream);

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

        layout = (LinearLayout) findViewById(R.id.listLayout);
        layout2 = (LinearLayout) findViewById(R.id.listLayout2);

        //keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem refreshItem = menu.findItem(R.id.action_editdream);
        refreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        refreshItem.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*
        if (id == R.id.action_settings) {
            //Intent intent = new Intent(Main.this, ClassName.class);
            //startActivity(intent);
            //setContentView(R.layout.layoutname);
        }
        */
        if (id == R.id.action_logout) {
            session.logoutUser();
            finish();
        }

        if (id == R.id.action_editdream) {
            Intent intent = new Intent(this, DreamyFormUpdate.class);
            intent.putExtra("key",selectedDreams);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_help) {
            HelpDialog();

        }

        if(id == android.R.id.home) {
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkDreamIndex(String[] dreamList, long[] dreamId) {
        selectedDreams = dreamId[selectedDreamIndex];
        if (dreamList[selectedDreamIndex] != null) {
            Log.e("index ", "amazingly not null");
            Dream dr = Dream.findById(Dream.class, dreamId[selectedDreamIndex]);
            milestonesSetUp(dr);
        } else {
            Log.e("index ", "is incredibly null");
            selectedDreamIndex = 0;
            Dream dr = Dream.findById(Dream.class, dreamId[selectedDreamIndex]);
            milestonesSetUp(dr);
        }
    }

    public void milestonesSetUp(final Dream dr) {
        layout.removeAllViewsInLayout();
        layout2.removeAllViewsInLayout();

        // MILESTONES
        // get miles from database
        Log.e("arrive here ", "LOH");

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(10, 10, 10, 10);
        TableRow.LayoutParams imglp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        imglp.setMargins(-50, 0, 0, 0);


        //get miles data
        List<Milestone> miles = Milestone.searchByDream(dr);
        final long[] milesId = new long[miles.size()];
        String[] milesList = new String[miles.size()];
        int incM = 0;

        for (final Milestone mil : miles) {
            Log.e("miles id", String.valueOf(mil.getId()));
            milesId[incM] = mil.getId();
            milesList[incM] = mil.getName();
            List<Todo> todos = Todo.searchByMilestone(mil);

            //set up table
            final TableLayout tl = new TableLayout(this);
            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10,10,10,10);
            tl.setLayoutParams(lp);
            tl.setPadding(0,10,10,10);

            //set up textview and image
            final TextView milest = new TextView(this);
            milest.setText(mil.getName());
            milest.setTypeface(null, Typeface.BOLD_ITALIC);

            //border layout
            Drawable rect = getResources().getDrawable(R.drawable.rectangle_border);
            tl.setBackground(rect);

            //random color
            final int rand = random();
            if(rand == 1){
                ((GradientDrawable)tl.getBackground()).setColor(Color.parseColor(colorPalette[rand]));//blue
            }else if(rand == 2){
                ((GradientDrawable)tl.getBackground()).setColor(Color.parseColor(colorPalette[rand]));//puspita color
            }else{
                ((GradientDrawable)tl.getBackground()).setColor(Color.parseColor(colorPalette[rand]));//green
            }

            if(todos.size()>=1){
                milest.setPaintFlags(milest.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }

            row.addView(milest);
            tl.addView(row);

            if(todos.size()<1){
                TableRow rowtd = new TableRow(this);
                rowtd.setGravity(Gravity.CENTER);
                TextView tdtxt = new TextView(this);
                tdtxt.setGravity(Gravity.CENTER);
                tdtxt.setText("No Task");
                tdtxt.setTextColor(Color.parseColor("#FFCCCCCC"));
                rowtd.addView(tdtxt);
                tl.addView(rowtd);
            }
            for(Todo td : todos){
                TableRow rowtd = new TableRow(this);
                rowtd.setGravity(Gravity.CENTER);
                TextView tdtxt = new TextView(this);
                tdtxt.setGravity(Gravity.CENTER);
                tdtxt.setText(td.getText().toString());
                if(td.getStatus()){
                    //tdtxt.setTextColor(Color.parseColor("#FF22C133"));
                    tdtxt.setPaintFlags(tdtxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }else{
                    tdtxt.setPaintFlags(tdtxt.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
                rowtd.addView(tdtxt);
                tl.addView(rowtd);
            }

            if(incM % 2 == 0) {
                layout.addView(tl);
            }
            else {
                layout2.addView(tl);
            }
            tl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(), milest.getText().toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Milestones.this, TaskList.class);
                    intent.putExtra("dream", dr.getId());
                    intent.putExtra("miles", mil.getId());
                    intent.putExtra("color", colorPalette[rand]);
                    startActivity(intent);
                    finish();
                }
            });
            incM++;
        }
    }

    public int random(){
        Random generator = new Random();
        int i = generator.nextInt(3) + 1;
        return i;
    }

    public void HelpDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //iv.setImageResource(R.drawable.help_main);
        dialog.setContentView(R.layout.help_main);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCanceledOnTouchOutside(true);
        //for dismissing anywhere you touch
        View masterView = dialog.findViewById(R.id.coach_mark_master_view);

        masterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
