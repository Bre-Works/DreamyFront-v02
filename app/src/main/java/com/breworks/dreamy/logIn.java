package com.breworks.dreamy;

/**
 * Created by Luck Eater on 10/2/2014.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.breworks.dreamy.DreamyLibrary.ConnectionManager;
import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.Logs;
import com.breworks.dreamy.model.dreamyAccount;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;

public class LogIn extends DreamyActivity {
    EditText usernameInput, passwordInput;
    public static Activity loginpage;
    ConnectionManager con;
    SessionManager session;

    HttpHelper httphelper;
    ProgressDialog progressDialog;

    String username, password;

    Calendar cal = Calendar.getInstance();
    java.util.Date cDate = cal.getTime();
    java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(cDate.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loginpage = this;

        session = new SessionManager(getApplicationContext());
        httphelper = new HttpHelper();
        con = new ConnectionManager(getApplicationContext());
        Log.e("INTERNET", String.valueOf(con.isConnectedToInternet()));
        //Check if there are login user or not
            if (session.isLoggedIn()) {
                Intent intent = new Intent(this, Main.class);
                Log.e("rightThere", String.valueOf(session.isLoggedIn()));
                new SendLogs().execute();
                startActivity(intent);
                finish();
            }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        Log.e("pol", "TEST");

        //Populate Data
        dreamyAccount ac1 = null;
        dreamyAccount ac2 = null;
        dreamyAccount ac3 = null;
         /*
        try {
            dreamyAccount.deleteAll(dreamyAccount.class);
            ac1 = dreamyAccount.createAccount("a@123.com", "a", "123");
            ac2 = dreamyAccount.createAccount("om@omi.com", "OMi", "123456");
            ac3 = dreamyAccount.createAccount("om@omu.com", "OMu", "123456");

            Dream.deleteAll(Dream.class);
            Dream dr1 = Dream.createDream("Conquer The World", false, ac1);
            Log.e("lol", dr1.getName());
            Log.e("dreamID", String.valueOf(dr1.getId()));
            Dream dr2 = Dream.createDream("Make a Homunculus", false, ac2);
            Log.e("pop", dr2.getName());
            Dream.createDream("IT PRO gets A", true, ac3);
            Dream.createDream("Accepted at UI", true, ac2);
            Dream.createDream("Around the World", true, ac1);

            Milestone.deleteAll(Milestone.class);
            Milestone mil1 = Milestone.createMilestone("Finish Database", false, dr1);
            Milestone.createMilestone("Finish UI", false, dr1);
            Milestone.createMilestone("Finish BackEnd", false, dr1);
            Milestone.createMilestone("Finish FrontEnd", false, dr1);

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
    }

    private class SendLogs extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Logs log = Logs.createLogs(session.getUser().getUsername());
                httphelper.POSTLogs(log);
            } catch (Exception e) {

            }
            return null;
        }
    }

    private class LoggingIn extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LogIn.this,"Please Wait...","Logging In.....");
            progressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                dreamyAccount dr = dreamyAccount.findByUsername(username);
                if(username.equals("") || password.equals("")){
                    progressDialog.dismiss();
                    return "EMPTY";
                }if(dr == null){
                    if (con.isConnectedToInternet()) {
                        dr = httphelper.findAccountByUserName(username);
                        dr = dreamyAccount.createAccount2(dr.getEmail(),dr.getUsername(),dr.getFirstName(),dr.getLastName(),currentTimestamp,dr.getPassword());
                        if(dr == null){
                            progressDialog.dismiss();
                            return "USERNAME";
                        }
                    }else{
                        progressDialog.dismiss();
                        return "USERNAMELOCAL";
                    }
                }if(dr !=null){
                    String userPass = dr.getPassword();
                    if (authentication(password, userPass)) {
                        dr.updateLastAccess(dr, currentTimestamp);

                        session.createLoginSession(dr.getUsername(), dr.getId());

                        new SendLogs().execute();
                        Intent intent = new Intent(LogIn.this, Main.class);
                        startActivity(intent);

                        finish();
                        return "IN";
                    }
                    else{
                        progressDialog.dismiss();
                        return "INVALIDPASS";
                    }
                }
            }catch(Exception e){
            }
            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            if(string == null){

            }else if (string.equals("INVALIDPASS")) {
                Toast.makeText(getApplicationContext(), "Invalid Password!", Toast.LENGTH_SHORT).show();
            } else if (string.equals("USERNAMELOCAL")) {
                Toast.makeText(getApplicationContext(), "Username does not exist in local machine!", Toast.LENGTH_SHORT).show();
            } else if (string.equals("USERNAME")) {
                Toast.makeText(getApplicationContext(), "Username does not exist!", Toast.LENGTH_SHORT).show();
            } else if (string.equals("EMPTY")) {
                Toast.makeText(getApplicationContext(), "Enter Something Please!", Toast.LENGTH_SHORT).show();
            } else if (string.equals("IN")){

            }
        }
    }

    public void loginAccount(View vi) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if(usernameInput.getText().toString().equals("")||passwordInput.getText().toString().equals("")){
            username = "";
            password = "";
        }else {
            username = usernameInput.getText().toString();
            password = passwordInput.getText().toString();
        }
        new LoggingIn().execute();
    }

    public static boolean authentication(String password, String userPass) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return PasswordHash.validatePassword(password, userPass);
    }




    public void goToMain(View vi) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        finish();
    }

    public void goToSignUp(View vi) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

}
