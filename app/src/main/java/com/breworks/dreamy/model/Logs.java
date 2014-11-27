package com.breworks.dreamy.model;

import java.sql.Timestamp;

public class Logs{
       String userId;
       Timestamp accessDate;
       String id;

        public Logs(){

        }

        public Logs(String userId, Timestamp accessDate, String id){
            this.userId = userId;
            this.accessDate = accessDate;
            this.id = id;
        }

        public String getUserID() {return this.userId;}

        public Timestamp getAccessDate() {return this.accessDate;}

        public String getId() {return this.id;}

}