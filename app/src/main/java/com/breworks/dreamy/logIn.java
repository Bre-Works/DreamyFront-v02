package com.breworks.dreamy;

/**
 * Created by Luck Eater on 10/2/2014.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.Milestone;
import com.breworks.dreamy.model.dreamyAccount;
import com.breworks.dreamy.SessionManager;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class logIn extends DreamyActivity {
    EditText usernameInput, passwordInput;
    public static Activity loginpage;

    SessionManager session;

    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loginpage = this;

        session = new SessionManager(getApplicationContext());

        //Check if there are login user or not
            if (session.isLoggedIn()) {
                Intent intent = new Intent(this, Main.class);
                Log.e("rightThere", String.valueOf(session.isLoggedIn()));
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
            Milestone mil1 = Milestone.createMilestone("Finish Database", true, dr1);
            Milestone.createMilestone("Finish UI", true, dr1);
            Milestone.createMilestone("Finish BackEnd", true, dr1);
            Milestone.createMilestone("Finish FrontEnd", true, dr1);

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
    }

    public void loginAccount(View vi) throws InvalidKeySpecException, NoSuchAlgorithmException {
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();
        Log.e("pip", username);
        Log.e("pop", password);

        try {
            if (!username.equals("") && dreamyAccount.findByUsername(username) == null) {
                Toast.makeText(getApplicationContext(), "Username does not exist!", Toast.LENGTH_SHORT).show();
            }
            if (dreamyAccount.find(dreamyAccount.class, "username = ?", username) != null) {
                dreamyAccount acc = dreamyAccount.findByUsername(username);

                String userPass = acc.getPassword();


                Log.e("lol", acc.getUsername());
                Log.e("pass", password);
                Log.e("encpass", acc.getPassword());

                if (authentication(password, userPass) == true) {
                    Intent intent = new Intent(this, Main.class);
                    startActivity(intent);

                    session.createLoginSession(acc.getUsername(), acc.getId());

                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Invalid Password!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("error", String.valueOf(e));
        }
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
