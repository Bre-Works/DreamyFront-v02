package com.breworks.dreamy.model;

import com.orm.SugarRecord;

/**
 * Created by Ryan on 05/10/2014.
 */
public class Milestone extends SugarRecord<Milestone> {

    String name;
    int status;

    //build relationship
    long dream_id;

    // constructors
    public Milestone() {
    }

    public Milestone(String name, int status) {
        this.name = name;
        this.status = status;
        this.dream_id = 0;
    }

    public Milestone(String name, int status, long dr) {
        this.name = name;
        this.status = status;
        this.dream_id = dr;
        this.save();
    }


    public String getName(){
        return this.name;
    }

    public int getStatus(){
        return this.status;
    }


}

