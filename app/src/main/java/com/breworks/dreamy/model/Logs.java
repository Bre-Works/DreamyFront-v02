package com.breworks.dreamy.model;

//import com.orm.SugarRecord;

public class Logs{
       String userId;
       String accessDate;
       String id;

        public Logs(){

        }

        public Logs(String userId, String accessDate, String id){
            this.userId = userId;
            this.accessDate = accessDate;
            this.id = id;
        }

        public String getUserID() {return this.userId;}

        public String getAccessDate() {return this.accessDate;}

        public String getId() {return this.id;}

}