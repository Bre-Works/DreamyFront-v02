package com.breworks.dreamy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.Milestone;
import com.breworks.dreamy.model.Todo;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Ryan Avila on 30/10/2014.
 */
public class DreamyFormUpdate extends DreamyActivity {

    LinearLayout container;
    ImageButton addMilestone;
    EditText milestoneInput;
    ImageButton removeMilestone;
    EditText dreamInput;
    SessionManager session;
    Dream dreamMain;

    long selectedDream = 0;

    List<String> mils = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {

        session = new SessionManager(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dreamy_form);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        milestoneInput = (EditText) findViewById(R.id.milestoneInput);
        container = (LinearLayout) findViewById(R.id.container);
        dreamInput = (EditText) findViewById(R.id.dreamInput);
        milestoneInput.setHint("add a new milestone");
        TextView title = (TextView) findViewById(R.id.title);
        title.setVisibility(View.INVISIBLE);

        TextView.OnEditorActionListener DoNothingOnEnter = new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    return true;
                }
                return false;
            }
        };
        dreamInput.setOnEditorActionListener(DoNothingOnEnter);

        Intent intent;
        try {
            intent = getIntent();
            long dream = intent.getLongExtra("key",0);
            selectedDream = dream;
            Log.e("lol", String.valueOf(dream));
            final Dream dr = Dream.findById(Dream.class,dream);
            dreamInput.setText(dr.getName());
            dreamMain = dr;

            dreamInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        dr.setName(String.valueOf(dreamInput.getText()));
                    }
                }
            });

            List<Milestone> miles = Milestone.searchByDream(dr);

            Log.e("lol1", String.valueOf(dr.getId()));

            for (final Milestone mil : miles) {
                final View addView = getLayoutInflater().inflate(R.layout.dreamy_form_row, null);

                removeMilestone = (ImageButton) addView.findViewById(R.id.delMilestone);

                CheckBox milestoneOut = (CheckBox) addView.findViewById(R.id.cbMilestone);

                mil.checkMilestoneStatus();
                if(mil.getStatus()){
                    milestoneOut.setChecked(true);
                }

                milestoneOut.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked())
                            mil.setStatus(true);
                        else
                            mil.setStatus(false);
                    }
                });

                final EditText editText = (EditText) addView.findViewById(R.id.Inputted);

                editText.setText(mil.getName());

                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            mil.setName(String.valueOf(editText.getText()));
                        }
                    }
                });

                removeMilestone.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v1) {
                        ((LinearLayout) addView.getParent()).removeView(addView);
                        mil.delete();
                    }
                });
                container.addView(addView);
            }
        }
        catch (Exception e){
            Log.e("error", String.valueOf(e));
        }

        milestoneInput.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        final View addView = getLayoutInflater().inflate(R.layout.dreamy_form_row, null);

                        removeMilestone = (ImageButton) addView.findViewById(R.id.delMilestone);

                        CheckBox milestoneOut = (CheckBox) addView.findViewById(R.id.cbMilestone);

                        final EditText editText = (EditText) addView.findViewById(R.id.Inputted);

                        editText.setText(milestoneInput.getText().toString());

                        String MileInput = milestoneInput.getText().toString();

                        final Milestone mil = Milestone.createMilestone(MileInput, false,dreamMain);

                        removeMilestone.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v1) {
                                ((LinearLayout) addView.getParent()).removeView(addView);
                                mil.delete();
                            }
                        });

                        container.addView(addView);
                        milestoneInput.setText("");

                        mils.add(MileInput);

                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem refreshItem = menu.findItem(R.id.action_deleteDream);
        refreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        refreshItem.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
        }

        if (id == R.id.action_deleteDream) {
            alertMessage();
        }

        return super.onOptionsItemSelected(item);
    }

    public void alertMessage() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                case DialogInterface.BUTTON_POSITIVE: // Yes button clicked

                    break;
                case DialogInterface.BUTTON_NEGATIVE: // No button clicked
                    List<Milestone> miles = Milestone.searchByDream(dreamMain);
                    for(Milestone mil : miles){
                        List<Todo> todo = Todo.searchByMilestone(mil);
                        for(Todo td : todo){
                            td.delete();
                        }
                        mil.delete();
                    }
                    dreamMain.delete();
                    Main.mainAct.finish();
                    Intent intent = new Intent(DreamyFormUpdate.this, Main.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(DreamyFormUpdate.this, "Dream deleted", Toast.LENGTH_LONG).show();
                    break; }
            } };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to remove this dream?")
                .setNegativeButton("Yes", dialogClickListener)
                .setPositiveButton("No", dialogClickListener).show(); }



    public void saveBackToHome(View v){
        Main.mainAct.finish();
        Intent intent = new Intent(this, Milestones.class);
        intent.putExtra("key", selectedDream);
        startActivity(intent);
        getCurrentFocus().clearFocus();
        finish();
    }

}