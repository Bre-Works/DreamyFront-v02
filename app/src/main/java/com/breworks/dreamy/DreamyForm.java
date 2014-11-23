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
import com.breworks.dreamy.model.dreamyAccount;

import java.util.ArrayList;
<<<<<<< HEAD
import java.util.Hashtable;
import java.util.LinkedList;
=======
>>>>>>> volley
import java.util.List;

/**
 * Created by arsianindita on 28-Sep-14.
 */

public class DreamyForm extends Activity {

    LinearLayout container;
    ImageButton addMilestone;
    EditText milestoneInput;
    ImageButton removeMilestone;
    EditText dreamInput;

    SessionManager session;

    List<String> miles = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {

        try {

            session = new SessionManager(getApplicationContext());

            super.onCreate(savedInstanceState);
            setContentView(R.layout.dreamy_form);
            milestoneInput = (EditText) findViewById(R.id.milestoneInput);
            container = (LinearLayout) findViewById(R.id.container);
            dreamInput = (EditText) findViewById(R.id.dreamInput);

            int i = 0;

            milestoneInput.setOnKeyListener(new View.OnKeyListener() {

<<<<<<< HEAD
        int i = 0;

        milestoneInput.setOnKeyListener(new View.OnKeyListener() {
=======
                public boolean onKey(View v, int keyCode, KeyEvent event) {
>>>>>>> volley

                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER) {
                            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService
                                    (Context.LAYOUT_INFLATER_SERVICE);

                            final View addView = inflater.inflate(R.layout.dreamy_form_row, null);

                            removeMilestone = (ImageButton) addView.findViewById(R.id.delMilestone);

                            CheckBox milestoneOut = (CheckBox) addView.findViewById(R.id.milestoneOut);

                            final EditText editText = (EditText) addView.findViewById(R.id.Inputted);

<<<<<<< HEAD
                        final String MileInput = milestoneInput.getText().toString();

                        milestoneOut.setText(MileInput);
                        miles.add(MileInput);
=======
                            final String MileInput = milestoneInput.getText().toString();
>>>>>>> volley

                            editText.setText(MileInput);
                            miles.add(MileInput);

<<<<<<< HEAD
                            @Override
                            public void onClick(View v1) {
                                ((LinearLayout) addView.getParent()).removeView(addView);
                                removeMile(MileInput);

                            }
                        });
=======
                            removeMilestone.setOnClickListener(new View.OnClickListener() {
>>>>>>> volley

                                @Override
                                public void onClick(View v1) {
                                    ((LinearLayout) addView.getParent()).removeView(addView);
                                    removeMile(MileInput);
                                }
                            });

<<<<<<< HEAD
=======
                            container.addView(addView);
                            milestoneInput.setText("");
>>>>>>> volley


                            return true;
                        }
                    }
                    return false;
                }
<<<<<<< HEAD
                return false;
            }
        });}
        catch(Exception e){
=======
            });
        } catch (Exception e) {
>>>>>>> volley
            Log.e("error", String.valueOf(e));
        }

    }

<<<<<<< HEAD
    public void removeMile(String mil){
        List<String> mm = new ArrayList<String>();
        for(String m : miles){
            if(!m.equals(mil)){
=======
    public void removeMile(String mil) {
        List<String> mm = new ArrayList<String>();
        for (String m : miles) {
            if (!m.equals(mil)) {
>>>>>>> volley
                mm.add(m);
                continue;
            }
        }
        miles = mm;
    }


<<<<<<< HEAD
    public void saveBackToHome(View v){
=======
    public void saveBackToHome(View v) {
>>>>>>> volley
        Intent intent = new Intent(this, Main.class);
        String dreamName = dreamInput.getText().toString();

        dreamyAccount dr = session.getUser();
        Dream dream = Dream.createDream(dreamName, false, dr);

        if (!dreamName.equals("")) {
            for (String m : miles) {
                Milestone.createMilestone(m, false, dream);
            }
        }
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}
