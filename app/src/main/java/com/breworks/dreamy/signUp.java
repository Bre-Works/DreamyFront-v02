package com.breworks.dreamy;

/**
 * Created by Luck Eater on 10/2/2014.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.breworks.dreamy.DreamyLibrary.ConnectionManager;
import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.dreamyAccount;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp extends DreamyActivity {
    Button createAccount;
    HttpHelper http;
    EditText usernameInput, emailInput, passwordInput, passwordConfInput, fNameInput, lNameInput;
    String username, email, password, passwordConf, firstName, lastName;
    ProgressDialog progressDialog;
    ConnectionManager con;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_up);
        http = new HttpHelper();
        con = new ConnectionManager(getApplicationContext());
        usernameInput = (EditText) findViewById(R.id.username);
        fNameInput = (EditText) findViewById(R.id.firstName);
        lNameInput = (EditText) findViewById(R.id.lastName);
        emailInput = (EditText) findViewById(R.id.email);
        passwordInput = (EditText) findViewById(R.id.password);
        passwordConfInput = (EditText) findViewById(R.id.passwordConf);
    }

    private class SigningUp extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SignUp.this, "Please Wait...", "Signing Up.....");
        }

        @Override
        protected String doInBackground(String... str) {
            List<dreamyAccount> allAccount = http.findAllAccount();
            if (http.searchAccountByUsername(allAccount, username) != null) {
                progressDialog.dismiss();
                return "USERNAMETAKEN";
            } else {
                if (checkPass(password) == false) {
                    progressDialog.dismiss();
                    return "PASSWORDLONG";
                } else {
                    if (!password.equals(passwordConf)) {
                        progressDialog.dismiss();
                        return "PASSWORDMATCH";
                    } else {
                        Calendar cal = Calendar.getInstance();
                        java.util.Date cDate = cal.getTime();
                        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(cDate.getTime());
                        dreamyAccount dc = null;
                        try {
                            dc = dreamyAccount.getAccount(allAccount, email, username, firstName, lastName, currentTimestamp, password);
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        http.POSTAccount(dc);

                        progressDialog.dismiss();

                        finish();
                        return "CREATED";
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String string) {
            if (string.equals("CREATED")) {
                Toast toast = Toast.makeText(getApplicationContext(), "Your account is now ready. Please login.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else if (string.equals("PASSWORDMATCH")) {
                Toast.makeText(getApplicationContext(), "Password and password confirmation did not match!", Toast.LENGTH_SHORT).show();
            } else if (string.equals("PASSWORDLONG")) {
                Toast toast = Toast.makeText(getApplicationContext(), "Password should have at least 6 character, uppercase, lowercase, and number", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else if (string.equals("USERNAMETAKEN")) {
                Toast toast = Toast.makeText(getApplicationContext(), "Username is already taken.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        }
    }

    public void createAccount(View v) throws InvalidKeySpecException, NoSuchAlgorithmException {
        email = emailInput.getText().toString();
        username = usernameInput.getText().toString();
        firstName = fNameInput.getText().toString();
        lastName = lNameInput.getText().toString();
        password = passwordInput.getText().toString();
        passwordConf = passwordConfInput.getText().toString();
        if(con.isConnectedToInternet()){
        if (!email.equals("") && !username.equals("") && !password.equals("") && !passwordConf.equals("")) {
            if (checkEmail(email) == false) {
                Toast toast = Toast.makeText(getApplicationContext(), "Invalid e-mail.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else {
                    new SigningUp().execute();
                }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Please complete the form.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }else{
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT);
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
