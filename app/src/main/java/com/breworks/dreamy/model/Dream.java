package com.breworks.dreamy.model;

import com.orm.SugarRecord;

/**
 * Created by Ryan on 05/10/2014.
 */
public class Dream extends SugarRecord<Dream>{

    String name;
    int status;

    //build the relationship
    long account_id;

    // constructors
    public Dream() {
    }

    public Dream(String name, int status) {
        this.name = name;
        this.status = status;
        this.account_id = 0;
    }

    public Dream(String name, int status,long acc) {
        this.name = name;
        this.status = status;
        this.account_id = acc;
    }

    public String getName(){
        return this.name;
    }

    public int getStatus(){
        return this.status;
    }
}
