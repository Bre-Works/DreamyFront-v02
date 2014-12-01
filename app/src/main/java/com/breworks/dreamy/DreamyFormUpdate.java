package com.breworks.dreamy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.Milestone;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Ryan Avila on 30/10/2014.
 */
public class DreamyFormUpdate extends Activity{

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


        milestoneInput = (EditText) findViewById(R.id.milestoneInput);
        container = (LinearLayout) findViewById(R.id.container);
        dreamInput = (EditText) findViewById(R.id.dreamInput);
        milestoneInput.setHint("add a new milestone");

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
                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);

                final View addView = inflater.inflate(R.layout.dreamy_form_row, null);

                removeMilestone = (ImageButton) addView.findViewById(R.id.delMilestone);

                CheckBox milestoneOut = (CheckBox) addView.findViewById(R.id.milestoneOut);

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
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService
                                (Context.LAYOUT_INFLATER_SERVICE);

                        final View addView = inflater.inflate(R.layout.dreamy_form_row, null);

                        removeMilestone = (ImageButton) addView.findViewById(R.id.delMilestone);

                        CheckBox milestoneOut = (CheckBox) addView.findViewById(R.id.milestoneOut);

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

    public void saveBackToHome(View v){
        Intent intent = new Intent(this, TodoSquare.class);
        intent.putExtra("key", selectedDream);
        startActivity(intent);
        getCurrentFocus().clearFocus();
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("key", selectedDream);
        startActivity(intent);
        getCurrentFocus().clearFocus();
        finish();
    }
}