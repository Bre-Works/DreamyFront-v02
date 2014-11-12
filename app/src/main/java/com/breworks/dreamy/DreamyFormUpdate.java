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
import android.widget.Toast;

import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.Milestone;
import com.breworks.dreamy.model.dreamyAccount;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.breworks.dreamy.SessionManager;

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

    List<String> mils = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {

        session = new SessionManager(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dreamy_form);


        milestoneInput = (EditText) findViewById(R.id.milestoneInput);
        container = (LinearLayout) findViewById(R.id.container);
        dreamInput = (EditText) findViewById(R.id.dreamInput);

        Intent intent;
        try {
            intent = getIntent();
            long dream = intent.getLongExtra("key",0);
            Log.e("lol", String.valueOf(dream));
            final Dream dr = Dream.findById(Dream.class,dream);
            dreamInput.setText(dr.getName());

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

                final EditText editText = (EditText) addView.findViewById(R.id.editText);

                editText.setText(mil.getName());

                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus) {
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

                        milestoneOut.setText(milestoneInput.getText().toString());

                        String MileInput = milestoneInput.getText().toString();

                        removeMilestone.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v1) {
                                ((LinearLayout) addView.getParent()).removeView(addView);
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

    public void removeMile(String mil){
        List<String> mm = new ArrayList<String>();
        for(String m : mils){
            if(!m.equals(mil)){
                mm.add(m);
                continue;
            }
        }
        mils = mm;
    }

    public void saveBackToHome(View v){
        Intent intent = new Intent(this, Main.class);
        String dreamName = dreamInput.getText().toString();

        dreamyAccount dr = session.getUser();
        Dream dream = Dream.createDream(dreamName,false,dr);

        if(!dreamName.equals("")){
            for(String m : mils){
                Milestone.createMilestone(m,false,dream);
            }
        }
        startActivity(intent);
        getCurrentFocus().clearFocus();
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        getCurrentFocus().clearFocus();
        finish();
    }
}

