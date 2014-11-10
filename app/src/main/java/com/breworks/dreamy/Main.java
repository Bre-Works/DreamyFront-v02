package com.breworks.dreamy;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.breworks.dreamy.tabpanel.MyTabHostProvider;
import com.breworks.dreamy.tabpanel.TabHostProvider;
import com.breworks.dreamy.tabpanel.TabView;

import java.util.List;

/**
 * Created by aidifauzan on 24-Sep-14.
 */

public class Main extends DreamyActivity {

    GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        TabHostProvider tabProvider = new MyTabHostProvider(Main.this);
        TabView tabView = tabProvider.getTabHost("Home");
        tabView.setCurrentView(R.layout.activity_main);
        setContentView(tabView.render(0));
        //ImageView bg = (ImageView) findViewById(R.id.bg);
        //bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        grid = (GridLayout) findViewById(R.id.grid);
        Drawable circle = getResources().getDrawable(R.drawable.circle);
        //circle.setBounds(0,0,100,100);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(10,10,10,10);

        Log.d("Reading: ", "Reading all contacts..");
        List<Dream> dreams = Dream.listAll(Dream.class);

        for (final Dream dr : dreams) {
            TextView Dc = new TextView(this);
            Dc.setBackground(circle);
            Dc.setPadding(50, 50, 50, 50);
            Dc.setLines(5);
            Dc.setLayoutParams(llp);
            Log.e("lol",dr.getName());
            if(!dr.getStatus()) {
                Dc.setText(dr.getName() + "\n- ONGOING " );
            }
            else{
                Dc.setText(dr.getName() + "\n- COMPLETED");
            }
            Dc.setGravity(Gravity.CENTER);
            Dc.isClickable();
            Dc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Main.this, DreamyFormUpdate.class);
                    intent.putExtra("key",dr.getId());
                    startActivity(intent);
                    finish();
                }
            });
            Dc.setTextAppearance(this, android.R.style.TextAppearance_Medium);
            //Dc.setHeight(Dc.getWidth());
            grid.addView(Dc);

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
            //Intent intent = new Intent(Main.this, ClassName.class);
            //startActivity(intent);
            //setContentView(R.layout.layoutname);
        }

        return super.onOptionsItemSelected(item);
    }

    public void gotoDreamyForm(View v){
        Intent intent = new Intent(this, DreamyForm.class);
        startActivity(intent);
        finish();
    }

}
