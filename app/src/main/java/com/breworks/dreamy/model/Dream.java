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
    dreamyAccount account;

    // constructors
    public Dream() {
    }

    public Dream(String name, boolean status,dreamyAccount acc) {
        this.dreamName = name;
        this.dreamStatus = status;
        this.account = acc;
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
    }

    public void setStatus(boolean status) {
        this.dreamStatus = status;
    }

    public static Dream findByDreamName(String DrName){
        List<Dream> dr = Dream.find(Dream.class, "dreamName = ?", DrName);
        if(dr.size() == 1){
            return dr.get(0);
        }
        else return null;
    }

    @Override
    public String toString() {
        return  dreamName;

    }

}

