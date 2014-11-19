package com.breworks.dreamy.model;

import com.breworks.dreamy.PasswordHash;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
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
    Timestamp tStamp;


    // constructors
    public dreamyAccount() {
    }

    public dreamyAccount(String email, String username, String firstName, String lastName, Timestamp tStamp, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.email = email;
        this.password = PasswordHash.createHash(password);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tStamp = tStamp;
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

    public static dreamyAccount createAccount(String email, String username, String firstName, String lastName, Timestamp tStamp, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        dreamyAccount account = new dreamyAccount(email, username, firstName, lastName, tStamp, password);
        account.save();
        return account;
    }

    public static dreamyAccount findByUsername(String Username) {
        List<dreamyAccount> acc = dreamyAccount.find(dreamyAccount.class, "username = ?", Username);
        if (acc.size() == 1) {
            return acc.get(0);
        } else return null;
    }

}

