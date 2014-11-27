package com.breworks.dreamy.model;

import android.content.Context;
import android.util.Log;

import com.breworks.dreamy.PasswordHash;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.breworks.dreamy.HttpHelper;

/**
 * Created by Luck Eater on 10/4/2014.
 */

public class dreamyAccount extends SugarRecord<dreamyAccount> {

    String email;
    String username;
    String password;
    String firstName;
    String lastName;
    Timestamp tsLastAccess;
    static HttpHelper http;

    // constructors
    public dreamyAccount() {
    }

    public dreamyAccount(String email, String username, String firstName, String lastName, Timestamp tsLastAccess, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.email = email;
        this.password = PasswordHash.createHash(password);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tsLastAccess = tsLastAccess;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public static dreamyAccount createAccount(Context applicationContext, String email, String username, String firstName, String lastName, Timestamp tsLastAccess, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        dreamyAccount account = new dreamyAccount(email, username, firstName, lastName, tsLastAccess, password);
        http = new HttpHelper(applicationContext);
        account.save();
        account.setId(setUniqueID(account.getId()));
        account.save();
        return account;
    }

    public static dreamyAccount findByUsername(String Username) {
        List<dreamyAccount> acc = dreamyAccount.find(dreamyAccount.class, "username = ?", Username);
        if (acc.size() == 1) {
            return acc.get(0);
        } else return null;
    }


    public static long setUniqueID(Long id){
        if(http.findAccountByID(id)== null){
            return id;
        }
        id++;
        return setUniqueID(id);
    }

    public void updateLastAccess(dreamyAccount acc, Timestamp lastAccess){
        acc.tsLastAccess = lastAccess;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Object getLastAccess() {
        return this.tsLastAccess;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastAccess(String lastAccess) {
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(lastAccess);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            this.tsLastAccess = timestamp;
        }catch(Exception e){
        }
    }
}

