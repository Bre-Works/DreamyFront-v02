package com.breworks.dreamy.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Ryan on 05/10/2014.
 */
public class Dream extends SugarRecord<Dream>{

    String dreamName;
    boolean dreamStatus;

    //build the relationship
    String account;

    // constructors
    public Dream() {
    }

    public Dream(String name, boolean status,dreamyAccount acc) {
        this.dreamName = name;
        this.dreamStatus = status;
        this.account = acc.getId().toString();
    }

    public String getName(){
        return this.dreamName;
    }

    public boolean getStatus(){
        return this.dreamStatus;
    }

    public static Dream createDream(String name, boolean status, dreamyAccount acc){
        Dream dream = new Dream(name,status,acc);
        dream.save();
        return dream;
    }

    public void setName(String dreamName){
        this.dreamName = dreamName;
        this.save();
    }

    public void setStatus(boolean status) {
        this.dreamStatus = status;
        this.save();
    }

    public void checkDreamStatus(){
        List<Milestone>miles = Milestone.searchByDream(this);
        boolean allClear = false;
        for(Milestone mil : miles){
            if(mil.getStatus()){
                allClear = true;
            } else{
                allClear = false;
            }
        }
        if(allClear){
            this.setStatus(true);
        }
    }

    public static Dream findByDreamName(String DrName){
        List<Dream> dr = Dream.find(Dream.class, "dreamName = ?", DrName);
        if(dr.size() == 1){
            return dr.get(0);
        }
        else return null;
    }

    public static List<Dream> searchByUser(dreamyAccount acc) {
        List<Dream> dr  = Dream.findWithQuery(Dream.class,"Select * from Dream Where account = ?", acc.getId().toString());
        return dr;
    }

    @Override
    public String toString() {
        return  dreamName;

    }

}

