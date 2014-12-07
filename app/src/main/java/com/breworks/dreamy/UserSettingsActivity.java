package com.breworks.dreamy;

/**
 * Created by Gregorius Adrian on 12/3/2014.
 */
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.breworks.dreamy.model.dreamyAccount;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class UserSettingsActivity extends PreferenceActivity {

    EditText firstNameForm, lastNameForm, oldPasswordForm, newPasswordForm, newPasswordConfForm;
    String firstName, lastName, oldPass, newPass, newPassConf;
    SessionManager session;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting);



        firstNameForm = (EditText) findViewById(R.id.setFirstName);
        lastNameForm = (EditText) findViewById(R.id.setLastName);
        oldPasswordForm = (EditText) findViewById(R.id.setOldPass);
        newPasswordForm =(EditText) findViewById(R.id.setNewPass);
        newPasswordConfForm = (EditText) findViewById(R.id.setNewPassConf);

        SessionManager session = new SessionManager(getApplicationContext());
        dreamyAccount currentAcc = session.getUser();

        firstNameForm.setText(currentAcc.getFirstName());
        lastNameForm.setText(currentAcc.getLastName());



    }

    public void saveChange() throws InvalidKeySpecException, NoSuchAlgorithmException {
        firstName = firstNameForm.getText().toString();
        lastName = lastNameForm.getText().toString();
        oldPass = oldPasswordForm.getText().toString();
        newPass = newPasswordForm.getText().toString();
        newPassConf = newPasswordConfForm.getText().toString();

        SessionManager session = new SessionManager(getApplicationContext());
        dreamyAccount currentAcc = session.getUser();

        String hashedOldPass = PasswordHash.createHash(oldPass);


        if(oldPass.equals("") && newPass.equals("") && newPassConf.equals("")){
            currentAcc.setFirstName(firstName);
            currentAcc.setLastName(lastName);
        } else{
            if(hashedOldPass.equals(currentAcc.getPassword())){
                if(newPass.equals(newPassConf)){
                    String hashedNewPass = PasswordHash.createHash(newPass);
                    currentAcc.setPassword(hashedNewPass);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password and password confirmation mismatch", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Wrong old password", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
