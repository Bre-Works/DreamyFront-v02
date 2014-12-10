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
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.breworks.dreamy.DreamyLibrary.ConnectionManager;
import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.dreamyAccount;
import com.breworks.dreamy.model.Logs;

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
                Log.e("MASUK","0");
                dreamyAccount dr = dreamyAccount.findByUsername(username);
                if(username.equals("") || password.equals("")){
                    Log.e("MASUK","1");
                    progressDialog.dismiss();
                    Looper.prepare();
                    MessageQueue queue = Looper.myQueue();
                    queue.addIdleHandler(new MessageQueue.IdleHandler() {
                        int mReqCount = 0;

                        @Override
                        public boolean queueIdle() {
                            if (++mReqCount == 2) {
                                // Quit looper
                                Looper.myLooper().quit();
                                return false;
                            } else
                                return true;
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Enter Something Please!", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }if(dr == null){
                    Log.e("MASUK","2");
                    if (con.isConnectedToInternet()) {
                        Log.e("MASUK","3");
                        dr = httphelper.findAccountByUserName(username);
                        dr = dreamyAccount.createAccount2(dr.getEmail(),dr.getUsername(),dr.getFirstName(),dr.getLastName(),currentTimestamp,dr.getPassword());
                        if(dr == null){
                            Log.e("MASUK","4");
                            progressDialog.dismiss();
                            Looper.prepare();
                            MessageQueue queue = Looper.myQueue();
                            queue.addIdleHandler(new MessageQueue.IdleHandler() {
                                int mReqCount = 0;

                                @Override
                                public boolean queueIdle() {
                                    if (++mReqCount == 2) {
                                        // Quit looper
                                        Looper.myLooper().quit();
                                        return false;
                                    } else
                                        return true;
                                }
                            });
                            Toast.makeText(getApplicationContext(), "Username does not exist!", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }else{
                        Log.e("MASUK","5");
                        progressDialog.dismiss();
                        Looper.prepare();
                        MessageQueue queue = Looper.myQueue();
                        queue.addIdleHandler(new MessageQueue.IdleHandler() {
                            int mReqCount = 0;

                            @Override
                            public boolean queueIdle() {
                                if (++mReqCount == 2) {
                                    // Quit looper
                                    Looper.myLooper().quit();
                                    return false;
                                } else
                                    return true;
                            }
                        });
                        Toast.makeText(getApplicationContext(), "Username does not exist in local machine!", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }if(dr !=null){
                    Log.e("MASUK","6");
                    String userPass = dr.getPassword();
                    Log.e("MASUK","9");
                    Log.e("lol", dr.getUsername());
                    Log.e("pass", password);
                    Log.e("encpass", dr.getPassword());
                    Log.e("MASUK","pass : "+ password);
                    Log.e("MASUK","pass : "+ userPass);
                    Log.e("MASUK","status : "+ authentication(password,userPass));

                    if (authentication(password, userPass)) {
                        Log.e("MASUK","7");
                        dr.updateLastAccess(dr, currentTimestamp);

                        session.createLoginSession(dr.getUsername(), dr.getId());
                        Log.e("ID MASUK SESSION", String.valueOf(dr.getId()));

                        new SendLogs().execute();
                        Intent intent = new Intent(LogIn.this, Main.class);
                        startActivity(intent);

                        finish();
                    }
                    else{
                        Log.e("MASUK","8");
                        progressDialog.dismiss();
                        Looper.prepare();
                        MessageQueue queue = Looper.myQueue();
                        queue.addIdleHandler(new MessageQueue.IdleHandler() {
                            int mReqCount = 0;

                            @Override
                            public boolean queueIdle() {
                                if (++mReqCount == 2) {
                                    // Quit looper
                                    Looper.myLooper().quit();
                                    return false;
                                } else
                                    return true;
                            }
                        });

                        Toast.makeText(getApplicationContext(), "Invalid Password!", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
            }catch(Exception e){
                Log.e("error", String.valueOf(e));
            }
            return null;
        }

        protected void onPostExecute(){
            progressDialog.dismiss();
        }

    }

    public void loginAccount(View vi) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if(usernameInput.getText().toString().equals("")||passwordInput.getText().toString().equals("")){
            username = "";
            password = "";
        }else {
            username = usernameInput.getText().toString();
            password = passwordInput.getText().toString();
            Log.e("pip", username);
            Log.e("pop", password);
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
