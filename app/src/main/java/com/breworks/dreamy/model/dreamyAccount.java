package com.breworks.dreamy.model;

import com.breworks.dreamy.HttpHelper;
import com.breworks.dreamy.PasswordHash;
import com.orm.SugarRecord;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        email = null;
        username = null;
        password = null;
        firstName = null;
        lastName = null;
        tsLastAccess = null;
    }

    public dreamyAccount(String email, String username, String firstName, String lastName, Timestamp tsLastAccess, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.email = email;
        this.password = password;
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

    public static dreamyAccount createAccount(String email, String username, String firstName, String lastName, Timestamp tsLastAccess, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        password = PasswordHash.createHash(password);
        dreamyAccount account = new dreamyAccount(email, username, firstName, lastName, tsLastAccess, password);
//        http = new HttpHelper();
//        account.save();
//        account.setId(setUniqueID(account.getId()));
        account.save();
        return account;
    }

    public static dreamyAccount createAccount2(String email, String username, String firstName, String lastName, Timestamp tsLastAccess, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        dreamyAccount account = new dreamyAccount(email, username, firstName, lastName, tsLastAccess, password);
//        http = new HttpHelper();
//        account.save();
//        account.setId(setUniqueID(account.getId()));
        account.save();
        return account;
    }

    public static dreamyAccount getAccount(List<dreamyAccount> acc,String email, String username, String firstName, String lastName, Timestamp tsLastAccess, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        dreamyAccount account = createAccount(email, username, firstName, lastName, tsLastAccess, password);
        account.setId(setUniqueID(acc,account.getId()));
        account.save();
        return account;
    }

    public static dreamyAccount findByUsername(String Username) {
        try {
            List<dreamyAccount> acc = dreamyAccount.find(dreamyAccount.class, "username = ?", Username);
            if (acc.size() >= 0) {
                return acc.get(0);
            } else return null;
        }catch (Exception e){
            return null;
        }
    }


    public static long setUniqueID(List<dreamyAccount> acc,Long id){
        for(dreamyAccount dd : acc){
            if(id == dd.getId()){
                id ++;
            }
        }
        return id;
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
        this.save();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.save();
    }

    public void setUsername(String username) {
        this.username = username;
        this.save();
    }

    public void setPassword(String password)  {
        try{
        password = PasswordHash.createHash(password);
        this.password = password;
        this.save();
        }
        catch (Exception e){

        }
    }

    public void setEmail(String email) {
        this.email = email;
        this.save();
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

