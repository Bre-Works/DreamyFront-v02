package com.breworks.dreamy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.dreamyAccount;

import java.util.List;

/**
 * Created by aidifauzan on 24-Sep-14.
 */

public class Main extends DreamyActivity {

    SessionManager session;
    GridLayout layout;
    dreamyAccount login;
    Display display;
    Point screenSize;
    int screenWidth;
    int screenHeight;
    int radius;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        session = new SessionManager(getApplicationContext());

        ActionBar actionBar = getActionBar();
        login = session.getUser();
        Log.e("ID MASUK MAIN", String.valueOf(login.getId()));
        setContentView(R.layout.activity_main);
        actionBar.setTitle("Hello, "+ login.getUsername());
        //ImageView bg = (ImageView) findViewById(R.id.bg);
        //bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        layout = (GridLayout) findViewById(R.id.listLayout);
        Drawable circle = getResources().getDrawable(R.drawable.circle_white);
        Drawable circle_completed = getResources().getDrawable(R.drawable.circle_completed);

        // Scaling
        display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);
        screenWidth = screenSize.x;
        screenHeight = screenSize.y;
        radius = (int) (screenWidth * 0.425);
        int margin = (int) (screenWidth * 0.05);

        RelativeLayout bgLayout = (RelativeLayout) findViewById(R.id.blurbg);
        Animation blurAnim = AnimationUtils.loadAnimation(this, R.anim.bluranim);
        TranslateAnimation randTransAnim;
        blurAnim.setRepeatMode(Animation.INFINITE);
        float fromX, toX, fromY, toY;
        for (int i = 0; i < bgLayout.getChildCount(); i++) {
            ImageView blur = (ImageView) bgLayout.getChildAt(i);
            fromX = 0;
            toX = (screenHeight * 0.9f) + Math.round(Math.random() * (screenHeight * -1.15f));
            fromY = screenHeight * 0.9f;
            toY = screenHeight * (-0.75f);
            randTransAnim = new TranslateAnimation(fromX, toX, fromY, toY);
            randTransAnim.setDuration(20000);
            //randTransAnim.setRepeatMode(Animation.INFINITE);
            randTransAnim.setRepeatCount(1000);
            AnimationSet animSet = new AnimationSet(true);
            animSet.addAnimation(randTransAnim);
            animSet.addAnimation(blurAnim);
            blur.startAnimation(animSet);
        }

        Log.d("Reading: ", "Reading all contacts..");
        List<Dream> dreams = Dream.searchByUser(login);
        int col = 0, row = 0;
        int colCount = 2;
        int rowCount = (dreams.size() / colCount) + 1;

        for (final Dream dr : dreams) {
            TextView textView = new TextView(this);
            textView.setWidth(radius);
            textView.setHeight(radius);
            textView.setPadding(20, 80, 20, 80);

            Log.e("lol", dr.getName());
            dr.checkDreamStatus();
            if (!dr.getStatus()) {
                textView.setText("\"" + dr.getName() + "\"" + "\n- ONGOING ");
                textView.setBackground(circle);
            } else {
                textView.setText("\"" + dr.getName() + "\"" + "\n- COMPLETED");
                textView.setBackground(circle_completed);
            }
            textView.setGravity(Gravity.CENTER);
            textView.isClickable();

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Main.this, Milestones.class);
                    intent.putExtra("key", dr.getId());
                    startActivity(intent);
                }
            });

            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(Main.this, DreamyFormUpdate.class);
                    intent.putExtra("key", dr.getId());
                    startActivity(intent);
                    return true;
                }
            });


            layout.setColumnCount(colCount);
            layout.setRowCount(rowCount);
            if (col == colCount) {
                col = 0;
                row++;
            }
            textView.setTextAppearance(this, android.R.style.TextAppearance_Small);

            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.setMargins(margin, margin, 0, 0);
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(col);
            param.rowSpec = GridLayout.spec(row);
            textView.setLayoutParams(param);
            layout.addView(textView);
            col++;
            Animation dreamsAnimation = AnimationUtils.loadAnimation(this, R.anim.dreamsanim);
            textView.startAnimation(dreamsAnimation);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem refreshItem = menu.findItem(R.id.action_createdream);
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
            /*dreamyAccount.deleteAll(dreamyAccount.class);
            Dream.deleteAll(Dream.class);
            Todo.deleteAll(Todo.class);
            Milestone.deleteAll(Milestone.class);*/
            finish();
        }
        if (id == R.id.action_createdream) {
            Intent intent = new Intent(this, DreamyForm.class);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_help) {
            //Intent intent = new Intent(this, Help.class);
            //startActivity(intent);
            HelpDialog();

        }

        return super.onOptionsItemSelected(item);
    }

    public void HelpDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //ImageView iv = (ImageView) findViewById(R.id.helpmainimageview);
        if(ViewConfiguration.get(this).hasPermanentMenuKey()){
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
        }else{
            //iv.setImageResource(R.drawable.help_main_softbutton);
            dialog.setContentView(R.layout.help_main2);
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

    public void gotoDreamyForm(View v) {
        Intent intent = new Intent(this, DreamyForm.class);
        startActivity(intent);
        finish();
    }

    //just for testing
    public void gotoTodo(View v) {
        Intent intent = new Intent(this, Milestones.class);
        startActivity(intent);
        finish();
    }

}
