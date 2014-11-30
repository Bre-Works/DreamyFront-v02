package com.breworks.dreamy.model;

import com.breworks.dreamy.HttpHelper;
import com.orm.SugarRecord;

import java.sql.Timestamp;
import java.util.Date;

public class Logs extends SugarRecord<Logs>{
       String username;
       Date accessDate;
       String LogId;
       static HttpHelper http = new HttpHelper();

        public Logs(){
        }

        public Logs(String username){
            this.username = username;
            Date nowadays = new Date();
            this.accessDate = nowadays;
        }

        public static Logs createLogs(String username){
            Logs log = new Logs(username);
            log.save();
            Logs.setUniqueID(log.getId());
            return log;
        }

        public String getUsername() {return this.username;}

        public Date getAccessDate() {return this.accessDate;}

        public static long setUniqueID(Long id){
            if(http.findAccountByID(id)== null){
                return id;
            }
            id++;
        return setUniqueID(id);
    }
}