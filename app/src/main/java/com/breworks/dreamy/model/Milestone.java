package com.breworks.dreamy.model;

import com.orm.SugarRecord;

/**
 * Created by Ryan on 05/10/2014.
 */
public class Milestone extends SugarRecord<Milestone> {

    String milesName;
    boolean milesStatus;

    //build relationship
    long dream_id;

    // constructors
    public Milestone() {
    }

    public Milestone(String name, boolean status, Dream dream) {
        this.milesName = name;
        this.milesStatus = status;
        this.dream_id = dream.getId();
        this.save();
    }


    public String getName(){
        return this.milesName;
    }

    public boolean getStatus(){
        return this.milesStatus;
    }

    public static Milestone createMilestone(String name, boolean status, Dream dream){
        Milestone miles = new Milestone(name,status,dream);
        miles.save();
        return miles;
    }

    public void setName(String milesName){
        this.milesName = milesName;
    }

    public void setStatus(boolean status) {
        this.milesStatus = status;
    }

}

