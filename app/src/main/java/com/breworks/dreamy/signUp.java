package com.breworks.dreamy;

/**
 * Created by Luck Eater on 10/2/2014.
 */

import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.dreamyAccount;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class signUp extends DreamyActivity {
    Button createAccount;
    EditText usernameInput, emailInput, passwordInput, passwordConfInput;
    String username, email, password, passwordConf;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_up);
        usernameInput = (EditText) findViewById(R.id.lastAccess);
        emailInput = (EditText) findViewById(R.id.email);
        passwordInput = (EditText) findViewById(R.id.password);
        passwordConfInput = (EditText) findViewById(R.id.passwordConf);
    }


    public void createAccount(View v) throws InvalidKeySpecException, NoSuchAlgorithmException {
        email = emailInput.getText().toString();
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();
        passwordConf = passwordConfInput.getText().toString();
        if (!email.equals("") && !username.equals("") && !password.equals("") && !passwordConf.equals("")) {
            if (checkEmail(email) == false) {
                Toast toast = Toast.makeText(getApplicationContext(), "Invalid e-mail.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else {
                if (dreamyAccount.findByUsername(username) != null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Username is already taken.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                } else {
                    if (checkPass(password) == false) {
                        Toast toast= Toast.makeText(getApplicationContext(), "Password should have at least 6 character, uppercase, lowercase, and number", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }else{
                            if (!password.equals(passwordConf)) {
                                Toast.makeText(getApplicationContext(), "Password and password confirmation did not match!", Toast.LENGTH_SHORT).show();
                            } else {
                                dreamyAccount.createAccount(email, username, password);
                                finish();
                                Toast toast= Toast.makeText(getApplicationContext(), "Your account is now ready. Please login.", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toast.show();
                            }
                        }
                    }
                }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Please complete the form.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }

    private boolean checkEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean checkPass(String password) {
        String pattern = "((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,})";
        Pattern p = Pattern.compile(pattern);

        Matcher m = p.matcher(password);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }


}
