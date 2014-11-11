package com.breworks.dreamy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.Milestone;
import com.breworks.dreamy.model.dreamyAccount;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by arsianindita on 28-Sep-14.
 */

public class DreamyForm extends Activity{

    LinearLayout container;
    ImageButton addMilestone;
    EditText milestoneInput;
    ImageButton removeMilestone;
    EditText dreamInput;
    SharedPreferences sharedPref;
    public static final String MyPREFERENCES = "DreamyPrefs" ;

    List<String> miles = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dreamy_form);
        milestoneInput = (EditText) findViewById(R.id.milestoneInput);
        container = (LinearLayout) findViewById(R.id.container);
        dreamInput = (EditText) findViewById(R.id.dreamInput);

        int i = 0;

        milestoneInput.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService
                                (Context.LAYOUT_INFLATER_SERVICE);

                        final View addView = inflater.inflate(R.layout.dreamy_form_row, null);

                        removeMilestone = (ImageButton) addView.findViewById(R.id.delMilestone);

                        EditText milestoneOut = (EditText) addView.findViewById(R.id.milestoneOut);

                        final String MileInput = milestoneInput.getText().toString();

                        milestoneOut.setText(MileInput);
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
        });}
        catch(Exception e){
            Log.e("error", String.valueOf(e));
        }

    }

    public void removeMile(String mil){
        List<String> mm = new ArrayList<String>();
        for(String m : miles){
            if(!m.equals(mil)){
                mm.add(m);
                continue;
            }
        }
        miles = mm;
    }


    public void saveBackToHome(View v){
        Intent intent = new Intent(this, Main.class);
        String dreamName = dreamInput.getText().toString();

        dreamyAccount dr = dreamyAccount.findById(dreamyAccount.class, sharedPref.getLong("DreamID",0));
        Dream dream = Dream.createDream(dreamName,false,dr);

        if(!dreamName.equals("")){
            for(String m : miles){
                Milestone.createMilestone(m,false,dream);
            }
        }
        startActivity(intent);
        finish();

    }



}