package com.breworks.dreamy.DreamyLibrary;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Maha on 10/30/14.
 */
public class DreamyActivity extends Activity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
