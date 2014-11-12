package com.breworks.dreamy.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

/**
 * Created by Ryan on 05/10/2014.
 */
public class Milestone extends SugarRecord<Milestone> {

    String milesName;
    boolean milesStatus;
    String dream;


    // constructors
    public Milestone() {
    }

    public Milestone(String name, boolean status, Dream dream) {
        this.milesName = name;
        this.milesStatus = status;
        this.dream = dream.getId().toString();
    }


    public String getName(){
        return this.milesName;
    }

    public boolean getStatus(){
        return this.milesStatus;
    }

    public String getDream() { return dream; }

    public static Milestone createMilestone(String name, boolean status, Dream dream){
        Milestone miles = new Milestone(name,status,dream);
        miles.save();
        return miles;
    }

    public void setName(String milesName){
        this.milesName = milesName;
        this.save();
    }

    public void setStatus(boolean status) {
        this.milesStatus = status;
        this.save();
    }


    public static List<Milestone> searchByDream(Dream dr) {
        List<Milestone> mil = Milestone.findWithQuery(Milestone.class,"Select * from Milestone Where dream = ?", dr.getId().toString());
        return mil;
    }
}

