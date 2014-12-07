package com.breworks.dreamy;

/**
 * Created by Gregorius Adrian on 12/3/2014.
 */
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.EditText;

public class UserSettingsActivity extends PreferenceActivity {

    EditText firstNameForm, lastNameForm, oldPasswordForm, newPasswordForm, newPasswordConfForm;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting);

        firstNameForm = (EditText) findViewById(R.id.setFirstName);
        lastNameForm = (EditText) findViewById(R.id.setLastName);
        oldPasswordForm = (EditText) findViewById(R.id.setOldPass);
        newPasswordForm =(EditText) findViewById(R.id.setNewPass);
        oldPasswordForm = (EditText) findViewById(R.id.setNewPassConf);





    }
}
