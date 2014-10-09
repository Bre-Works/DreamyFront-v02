package com.breworks.dreamy.model;

import com.orm.SugarRecord;

/**
 * Created by Ryan on 05/10/2014.
 */
public class Todo extends SugarRecord<Todo>{

        String name;
        int status;

        //build relationship
        long miles_id;

        // constructors
        public Todo() {
        }

        public Todo(String name, int status) {
            this.name = name;
            this.status = status;
            this.miles_id = 0;
        }

        public Todo(String name, int status, long mil) {
            this.name = name;
            this.status = status;
            this.miles_id = mil;
            this.save();
        }

        public String getName(){
            return this.name;
        }

        public int getStatus(){
            return this.status;
        }

}
