package com.breworks.dreamy;

import android.app.Activity;
import android.content.Context;
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

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.Milestone;
import com.breworks.dreamy.model.dreamyAccount;

import java.util.ArrayList;
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
            getActionBar().setDisplayHomeAsUpEnabled(true);
            milestoneInput = (EditText) findViewById(R.id.milestoneInput);
            container = (LinearLayout) findViewById(R.id.container);
            dreamInput = (EditText) findViewById(R.id.dreamInput);

            int i = 0;

            TextView.OnEditorActionListener DoNothingOnEnter = new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                        return true;
                    }
                    return false;
                }
            };
            dreamInput.setOnEditorActionListener(DoNothingOnEnter);

            milestoneInput.setOnKeyListener(new View.OnKeyListener() {

                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER) {
                            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService
                                    (Context.LAYOUT_INFLATER_SERVICE);

                            final View addView = inflater.inflate(R.layout.dreamy_form_row, null);

                            removeMilestone = (ImageButton) addView.findViewById(R.id.delMilestone);

                            CheckBox milestoneOut = (CheckBox) addView.findViewById(R.id.cbMilestone);

                            final EditText editText = (EditText) addView.findViewById(R.id.Inputted);

                            final String MileInput = milestoneInput.getText().toString();

                            editText.setText(MileInput);
                            miles.add(MileInput);

                            removeMilestone.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v1) {
                                    ((LinearLayout) addView.getParent()).removeView(addView);
                                    removeMile(MileInput);
                                }
                            });

                            container.addView(addView);
                            milestoneInput.setText("");


                            return true;
                        }
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            Log.e("error", String.valueOf(e));
        }

    }

    public void removeMile(String mil) {
        List<String> mm = new ArrayList<String>();
        for (String m : miles) {
            if (!m.equals(mil)) {
                mm.add(m);
                continue;
            }
        }
        miles = mm;
    }


    public void saveBackToHome() {
        Main.mainAct.finish();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem refreshItem = menu.findItem(R.id.action_saveDream);
        refreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        refreshItem.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_saveDream) {
            saveBackToHome();
        }

        if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}