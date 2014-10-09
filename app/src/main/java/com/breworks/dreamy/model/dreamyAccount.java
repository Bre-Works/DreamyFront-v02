package com.breworks.dreamy.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Luck Eater on 10/4/2014.
 */

public class dreamyAccount extends SugarRecord<dreamyAccount> {

        String email;
        String username;
        String password;

        // constructors
        public dreamyAccount() {
        }

        public dreamyAccount(String email, String username ,String password) {
            this.email = email;
            this.password = password;
            this.username = username;
        }

        public String getEmail(){
            return this.email;
        }

        public String getPassword(){
            return this.password;
        }

        public String getUsername(){
            return this.username;
        }

        public static dreamyAccount createAccount(String email, String username, String password){
            dreamyAccount account = new dreamyAccount(email, username, password);
            account.save();
            return account;
        }

        public static dreamyAccount findByUsername(String Username){
            List<dreamyAccount> acc = dreamyAccount.find(dreamyAccount.class,"username = ?",Username);
            if(acc.size() == 1){
                return acc.get(0);
            }
            else return null;
        }

}

