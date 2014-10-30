package com.breworks.dreamy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.Milestone;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

/**
 * Created by arsianindita on 28-Sep-14.
 */

public class DreamyForm extends DreamyActivity {

    LinearLayout container;
    ImageButton addMilestone;
    EditText milestoneInput;
    ImageButton removeMilestone;
    EditText dreamInput;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dreamy_form);
        milestoneInput = (EditText) findViewById(R.id.milestoneInput);
        container = (LinearLayout) findViewById(R.id.container);
        dreamInput = (EditText) findViewById(R.id.dreamInput);

        Intent intent;
        if(getIntent() != null) {
            intent = getIntent();
            long dream = intent.getLongExtra("key",0);
            Log.e("lol", String.valueOf(dream));
            Dream dr = Dream.findById(Dream.class,dream);
            dreamInput.setText(dr.getName());

            try {
                List<Milestone> miles = Select.from(Milestone.class)
                                        .where(Condition.prop("dream_id").eq(dr.getId()))
                                        .list();
                Log.e("lol", String.valueOf(dr.getId()));

                for (Milestone mil : miles) {
                    LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService
                            (Context.LAYOUT_INFLATER_SERVICE);

                    final View addView = inflater.inflate(R.layout.dreamy_form_row, null);

                    removeMilestone = (ImageButton) addView.findViewById(R.id.delMilestone);

                    EditText milestoneOut = (EditText) addView.findViewById(R.id.milestoneOut);

                    milestoneOut.setText(mil.getName());

                    Log.e("la",mil.getName());
                    removeMilestone.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v1) {
                            ((LinearLayout) addView.getParent()).removeView(addView);
                        }
                    });

                    container.addView(addView);
                }
            }
            catch (Exception e){
                Log.e("error", String.valueOf(e));
            }
        }

        milestoneInput.setOnKeyListener(new View.OnKeyListener() {

                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER) {
                            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService
                                    (Context.LAYOUT_INFLATER_SERVICE);

                            final View addView = inflater.inflate(R.layout.dreamy_form_row, null);

                            removeMilestone = (ImageButton) addView.findViewById(R.id.delMilestone);

                            EditText milestoneOut = (EditText) addView.findViewById(R.id.milestoneOut);

                            milestoneOut.setText(milestoneInput.getText().toString());

                            removeMilestone.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v1) {
                                    ((LinearLayout) addView.getParent()).removeView(addView);
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
    }

    public void saveBackToHome(View v){
        finish();
    }

}

