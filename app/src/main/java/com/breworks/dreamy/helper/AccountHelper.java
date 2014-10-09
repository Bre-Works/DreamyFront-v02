package com.breworks.dreamy.helper;

import com.breworks.dreamy.model.dreamyAccount;

import java.util.List;

/**
 * Created by Ryan on 09/10/2014.
 */
public class AccountHelper {

    public static void createAccount(String email, String username, String password){
        dreamyAccount account = new dreamyAccount(email, username, password);
        account.save();
    }

    public static dreamyAccount findByUsername(String Username){
        List<dreamyAccount> acc = dreamyAccount.find(dreamyAccount.class,"username = ?",Username);
        if(acc.size() == 1){
            return acc.get(0);
        }
        else return null;
    }
}
