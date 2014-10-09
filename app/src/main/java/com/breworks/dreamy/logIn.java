package com.breworks.dreamy;

/**
 * Created by Luck Eater on 10/2/2014.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.Milestone;
import com.breworks.dreamy.model.dreamyAccount;

public class logIn extends Activity {
    EditText usernameInput, passwordInput;
    String username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        Log.e("pol","TEST");

        //Populate Data
            dreamyAccount.deleteAll(dreamyAccount.class);
            dreamyAccount ac1 = dreamyAccount.createAccount("om@om.com", "OM", "123456");
            dreamyAccount ac2 = dreamyAccount.createAccount("om@omi.com", "OMi", "123456");
            dreamyAccount ac3 = dreamyAccount.createAccount("om@omu.com", "OMu", "123456");

            Dream.deleteAll(Dream.class);
            Dream dr1 = Dream.createDream("Conquer The World", false, ac1);
            Log.e("lol", dr1.getName());
            Dream dr2 = Dream.createDream("Make a Homunculus", false,ac2);
            Log.e("pop", dr2.getName());
            Dream.createDream("IT PRO gets A", true,ac3);
            Dream.createDream("Accepted at UI", true,ac2);
            Dream.createDream("Around the World", true,ac1);

            Milestone.deleteAll(Milestone.class);
            Milestone a = new Milestone("Finish Database", true, dr1);
            Milestone b = new Milestone("Finish UI", true, dr1);
            Milestone c = new Milestone("Finish BackEnd", true, dr1);
            Milestone d = new Milestone("Finish FrontEnd", true, dr1);


        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
    }

    public void loginAccount (View vi){
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();
        Log.e("pip",username);
        Log.e("pop", password);
        try{
        if(dreamyAccount.find(dreamyAccount.class,"username = ?",username) != null){
            dreamyAccount acc = dreamyAccount.findByUsername(username);
            Log.e("lol",acc.getUsername());
            Log.e("pop", password);
            if(password.equals(acc.getPassword())){
                Intent intent = new Intent(this, Main.class);
                startActivity(intent);
            }
        }}
        catch (Exception e){
            Log.e("error", String.valueOf(e));
        }
    }

    public void goToMain(View vi){
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        finish();
    }

    public void goToSignUp(View vi){
        Intent intent = new Intent(this, signUp.class);
        startActivity(intent);
        finish();
    }

}
