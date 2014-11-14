package com.breworks.dreamy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

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
    LinearLayout layout;
    dreamyAccount login;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        session = new SessionManager(getApplicationContext());

        ActionBar actionBar = getActionBar();
        login = session.getUser();

        actionBar.setTitle("Hello, "+ login.getUsername());

        TabHostProvider tabProvider = new MyTabHostProvider(Main.this);
        TabView tabView = tabProvider.getTabHost("Home");
        tabView.setCurrentView(R.layout.activity_main);
        setContentView(tabView.render(0));
        //ImageView bg = (ImageView) findViewById(R.id.bg);
        //bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        layout = (LinearLayout) findViewById(R.id.listLayout);
        Drawable rect = getResources().getDrawable(R.drawable.rectangle);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(30, 30, 30, 30);

        Log.d("Reading: ", "Reading all contacts..");
        List<Dream> dreams = Dream.searchByUser(login);

        for (final Dream dr : dreams) {
            TextView Dc = new TextView(this);
            Dc.setBackground(rect);
            Dc.setPadding(60,10,60,10);
            Dc.setLayoutParams(llp);
            Log.e("lol", dr.getName());
            if (!dr.getStatus()) {
                Dc.setText(dr.getName() + "\n- ONGOING ");
            } else {
                Dc.setText(dr.getName() + "\n- COMPLETED");
            }
            Dc.setGravity(Gravity.CENTER);
            Dc.isClickable();
            Dc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Main.this, DreamyFormUpdate.class);
                    intent.putExtra("key", dr.getId());
                    startActivity(intent);
                    finish();
                }
            });
            Dc.setTextAppearance(this, android.R.style.TextAppearance_Medium);
            layout.addView(Dc);
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
        //if (id == R.id.action_todolist) {
        //    Intent intent = new Intent(this, ToDoList.class);
        //    startActivity(intent);
        //    finish();
        //}

        return super.onOptionsItemSelected(item);
    }

    public void gotoDreamyForm(View v) {
        Intent intent = new Intent(this, DreamyForm.class);
        startActivity(intent);
        finish();
    }

}
