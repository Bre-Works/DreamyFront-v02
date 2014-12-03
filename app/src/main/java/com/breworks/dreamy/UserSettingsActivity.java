package com.breworks.dreamy;

/**
 * Created by Gregorius Adrian on 12/3/2014.
 */
    import android.os.Bundle;
    import android.preference.PreferenceActivity;

public class UserSettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

    }
}
