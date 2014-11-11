package com.breworks.dreamy;

import android.app.Application;

import com.orm.SugarApp;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Maha on 10/30/14.
 */
public class Dreamy extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault("fonts/Existence-Light/Existence-Light.otf", R.attr.fontPath);
    }
}
