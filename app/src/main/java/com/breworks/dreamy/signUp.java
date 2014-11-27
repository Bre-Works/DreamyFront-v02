package com.breworks.dreamy;

/**
 * Created by Luck Eater on 10/2/2014.
 */

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.dreamyAccount;
import com.breworks.dreamy.HttpHelper;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class signUp extends DreamyActivity {
    Button createAccount;
    HttpHelper http;
    EditText usernameInput, emailInput, passwordInput, passwordConfInput, fNameInput, lNameInput;
    String username, email, password, passwordConf, firstName, lastName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_up);
        http = new HttpHelper(getApplicationContext());
        usernameInput = (EditText) findViewById(R.id.username);
        fNameInput = (EditText) findViewById(R.id.firstName);
        lNameInput = (EditText) findViewById(R.id.lastName);
        emailInput = (EditText) findViewById(R.id.email);
        passwordInput = (EditText) findViewById(R.id.password);
        passwordConfInput = (EditText) findViewById(R.id.passwordConf);
    }

    public void createAccount(View v) throws InvalidKeySpecException, NoSuchAlgorithmException {
        email = emailInput.getText().toString();
        username = usernameInput.getText().toString();
        firstName = fNameInput.getText().toString();
        lastName = lNameInput.getText().toString();
        password = passwordInput.getText().toString();
        passwordConf = passwordConfInput.getText().toString();
        new ProggressD(getApplicationContext());
        if (!email.equals("") && !username.equals("") && !password.equals("") && !passwordConf.equals("")) {
            if (checkEmail(email) == false) {
                Toast toast = Toast.makeText(getApplicationContext(), "Invalid e-mail.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else {
                if (http.findAccountByUserName(username) != null) {
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
                                Calendar cal = Calendar.getInstance();
                                java.util.Date cDate = cal.getTime();
                                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(cDate.getTime());
                                Log.e("timestamp", String.valueOf(currentTimestamp));
                                dreamyAccount dc = dreamyAccount.createAccount(getApplicationContext(),email, username, firstName, lastName, currentTimestamp, password);
                                http.POSTAccount(dc);
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
