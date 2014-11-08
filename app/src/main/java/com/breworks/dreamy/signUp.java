package com.breworks.dreamy;

/**
 * Created by Luck Eater on 10/2/2014.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.breworks.dreamy.model.dreamyAccount;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class signUp extends Activity {
    Button createAccount;
    EditText usernameInput, emailInput, passwordInput, passwordConfInput;
    String username, email, password, passwordConf;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_up);
        usernameInput = (EditText) findViewById(R.id.username);
        emailInput = (EditText) findViewById(R.id.email);
        passwordInput = (EditText) findViewById(R.id.password);
        passwordConfInput = (EditText) findViewById(R.id.passwordConf);
    }


    public void createAccount(View v)  throws InvalidKeySpecException, NoSuchAlgorithmException{
        email = emailInput.getText().toString();
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();
        passwordConf = passwordConfInput.getText().toString();
        if(!email.equals("") && !username.equals("") && !password.equals("") && !passwordConf.equals("")){
            if (dreamyAccount.findByUsername(username) != null) {
                Toast.makeText(getApplicationContext(), "Username is already taken.", Toast.LENGTH_SHORT).show();
            } else {
                if (!password.equals(passwordConf)) {
                    Toast.makeText(getApplicationContext(), "Password and password confirmation did not match!", Toast.LENGTH_SHORT).show();
                } else {
                    dreamyAccount.createAccount(email, username, password);
                    finish();
                    Toast.makeText(getApplicationContext(), "Your account is now ready. Please login.", Toast.LENGTH_LONG).show();
                }
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please complete the form.", Toast.LENGTH_SHORT).show();
        }
    }

}
