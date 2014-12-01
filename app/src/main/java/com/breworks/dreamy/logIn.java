package com.breworks.dreamy;

/**
 * Created by Luck Eater on 10/2/2014.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class logIn extends DreamyActivity {
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
            progressDialog = ProgressDialog.show(logIn.this,"Please Wait...","Logging In.....");
            progressDialog.setCancelable(true);

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if(con.isConnectedToInternet()){
                dreamyAccount dr = httphelper.findAccountByUserName(username);
                if (!username.equals("") && dr == null) {
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

                if (dr != null) {
                    dreamyAccount acc;
                    if(dreamyAccount.findByUsername(username) == null){
                        acc = dr;
                        dreamyAccount ac = dreamyAccount.getAccount(acc.getEmail(), acc.getUsername(), acc.getFirstName(), acc.getLastName(), currentTimestamp, acc.getPassword());
                        Log.e("ID BELUM MASUK & GANTI", String.valueOf(acc.getId()));
                        ac.setId(acc.getId());
                        ac.save();
                        Log.e("ID BELUM MASUK", String.valueOf(acc.getId()));
                    }
                    else {
                        acc = dreamyAccount.findByUsername(username);
                    }

                    String userPass = acc.getPassword();
                    Log.e("ID DI DATABASE",String.valueOf(dreamyAccount.findByUsername(acc.getUsername()).getId()));
                    //String userID = acc.getUsername();
                    //String accessDate = acc.getAccessDate();
                    //Logs log = new Logs(userID,accessDate,userID);

                    //HttpHelper.POSTtoLogs(log);


                    Log.e("lol", acc.getUsername());
                    Log.e("pass", password);
                    Log.e("encpass", acc.getPassword());

                    if (authentication(password, userPass) == true) {
                        acc.updateLastAccess(acc, currentTimestamp);

                        session.createLoginSession(acc.getUsername(), acc.getId());
                        Log.e("ID MASUK SESSION", String.valueOf(acc.getId()));

                        new SendLogs().execute();

                        Intent intent = new Intent(logIn.this, Main.class);
                        startActivity(intent);

                        finish();
                    }
                    else{
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
                }else{
                    dreamyAccount dr = dreamyAccount.findByUsername(username);
                    if (!username.equals("") && dr == null) {
                        this.cancel(true);
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

                        Toast.makeText(getApplicationContext(), "Username does not exist in this Device!", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Log.e("MASUK", "MASUK1");


                    }
                    if (dr != null) {
                        dreamyAccount acc;
                        acc = dr;
                        Log.e("MASUK", "MASUK2");
                        String userPass = acc.getPassword();
                        Log.e("ID DI DATABASE", String.valueOf(dreamyAccount.findByUsername(acc.getUsername()).getId()));
                        //String userID = acc.getUsername();
                        //String accessDate = acc.getAccessDate();
                        //Logs log = new Logs(userID,accessDate,userID);

                        //HttpHelper.POSTtoLogs(log);


                        Log.e("lol", acc.getUsername());
                        Log.e("pass", password);
                        Log.e("encpass", acc.getPassword());

                        if (authentication(password, userPass) == true) {
                            acc.updateLastAccess(acc, currentTimestamp);
                            Log.e("MASUK","MASUK3");
                            session.createLoginSession(acc.getUsername(), acc.getId());
                            Log.e("ID MASUK SESSION", String.valueOf(acc.getId()));

                            new SendLogs().execute();

                            Intent intent = new Intent(logIn.this, Main.class);
                            startActivity(intent);

                            finish();
                        }
                        else{
                            progressDialog.dismiss();
                            Looper.prepare();
                            Log.e("MASUK","MASUK4");
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
                }
                Log.e("MASUK","MASUK5");
            }catch(NullPointerException ex){
                Toast.makeText(getApplicationContext(),"No Username Found",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("error", String.valueOf(e));
            }
            return null;
        }

        protected void onPostExecute(){
            progressDialog.dismiss();
        }

    }

    public void loginAccount(View vi) throws InvalidKeySpecException, NoSuchAlgorithmException {
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();
        Log.e("pip", username);
        Log.e("pop", password);
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
        Intent intent = new Intent(this, signUp.class);
        startActivity(intent);
    }

}
