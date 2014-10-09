package com.breworks.dreamy;

/**
 * Created by Luck Eater on 10/2/2014.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.breworks.dreamy.helper.AccountHelper;
import com.breworks.dreamy.model.dreamyAccount;

public class logIn extends Activity {
    EditText usernameInput, passwordInput;
    String username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        //Populate Data
        if(dreamyAccount.listAll(dreamyAccount.class) == null) {
            AccountHelper.createAccount("om@om.com", "OM", "123456");
            AccountHelper.createAccount("om@omi.com", "OMi", "123456");
            AccountHelper.createAccount("om@omu.com", "OMu", "123456");
        }
        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        finish();
    }

    public void loginAccount (View vi){
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();
        if(dreamyAccount.find(dreamyAccount.class,"username = ?",username) != null){
            dreamyAccount acc = AccountHelper.findByUsername(username);
            if(password == acc.getPassword()){
                Intent intent = new Intent(this, Main.class);
                startActivity(intent);
            }
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
    }

}
