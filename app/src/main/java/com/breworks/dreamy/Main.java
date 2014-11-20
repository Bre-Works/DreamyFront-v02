package com.breworks.dreamy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.dreamyAccount;
import com.breworks.dreamy.tabpanel.MyTabHostProvider;
import com.breworks.dreamy.tabpanel.TabHostProvider;
import com.breworks.dreamy.tabpanel.TabView;
import com.breworks.dreamy.SessionManager;

import java.util.List;

/**
 * Created by aidifauzan on 24-Sep-14.
 */

public class Main extends DreamyActivity {

    SessionManager session;
    TableLayout table;
    dreamyAccount login;
    final boolean EVEN = false;
    final boolean ODD = true;
    boolean pattern = EVEN;
    final boolean LEFT = false;
    final boolean RIGHT = true;
    boolean position = LEFT;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        session = new SessionManager(getApplicationContext());

        ActionBar actionBar = getActionBar();
        login = session.getUser();
        setContentView(R.layout.activity_main);

        //actionBar.setTitle("Hello, "+ login.getUsername());

        //TabHostProvider tabProvider = new MyTabHostProvider(Main.this);
        //TabView tabView = tabProvider.getTabHost("Home");
        //tabView.setCurrentView(R.layout.activity_main);
        //setContentView(tabView.render(0));
        //ImageView bg = (ImageView) findViewById(R.id.bg);
        //bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        Drawable shape = getResources().getDrawable(R.drawable.diamond);
        table = (TableLayout) findViewById(R.id.dreamsLayout);
        TableRow row = new TableRow(this);
        shape.setBounds(250,250,250,250);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(30, 30, 30, 30);

        LinearLayout.LayoutParams shift = new LinearLayout.LayoutParams(250, 250);
        shift.setMargins(250,0,0,0);

        Log.d("Reading: ", "Reading all contacts..");
        List<Dream> dreams = Dream.listAll(Dream.class);

        for (final Dream dr : dreams) {
            TextView textView = new TextView(this);
            //textView.setBackground(shape);
            //textView.setPadding(60,10,60,10);
            //textView.setLayoutParams(llp);
            
            Log.e("lol", dr.getName());
            if (!dr.getStatus()) {
                textView.setText(dr.getName() + "\n- ONGOING ");
            } else {
                textView.setText(dr.getName() + "\n- COMPLETED");
            }
            textView.setGravity(Gravity.CENTER);
            textView.isClickable();
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Main.this, DreamyFormUpdate.class);
                    intent.putExtra("key", dr.getId());
                    startActivity(intent);
                    finish();
                }
            });
            textView.setTextAppearance(this, android.R.style.TextAppearance_Small);

            //row.addView(textView);
            //table.addView(textView);

            if (pattern == EVEN) {
                if (position == LEFT) {
                    //row = new TableRow(this);
                    //row.addView(textView);
                    //table.addView(row);
                    textView.setBackground(shape);
                    table.addView(textView);
                    position = RIGHT;
                } else { //RIGHT
                    textView.setLayoutParams(shift);
                    textView.setBackground(shape);
                    //row.addView(textView);
                    table.addView(textView);
                    position = LEFT;
                    pattern = ODD;
                }
            } else { // ODD
                textView.setLayoutParams(shift);
                textView.setBackground(shape);
                //row = new TableRow(this);
                //row.addView(textView);
                //table.addView(row);
                table.addView(textView);
                pattern = EVEN;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //Intent intent = new Intent(Main.this, ClassName.class);
            //startActivity(intent);
            //setContentView(R.layout.layoutname);
        }
        if (id == R.id.action_logout) {
            session.logoutUser();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void gotoDreamyForm(View v) {
        Intent intent = new Intent(this, DreamyForm.class);
        startActivity(intent);
        finish();
    }

}
