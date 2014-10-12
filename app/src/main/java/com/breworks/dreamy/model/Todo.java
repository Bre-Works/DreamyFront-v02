package com.breworks.dreamy.model;

import com.orm.SugarRecord;

/**
 * Created by Ryan on 05/10/2014.
 */
public class Todo extends SugarRecord<Todo> {

    String todoText;
    boolean todoStatus;

    //build relationship
    Milestone miles;

    // constructors
    public Todo() {
    }

    public Todo(String text) {
        this.todoText = text;
        this.todoStatus = false;
        this.save();
    }

    public Todo(String name, boolean status, Milestone mil) {
        this.todoText = name;
        this.todoStatus = status;
        this.miles = mil;
        this.save();
    }

    public String getText() {
        return this.todoText;
    }

    public boolean getStatus() {
        return this.todoStatus;
    }

    public static Todo createMilestone(String name, boolean status, Milestone miles) {
        Todo todo = new Todo(name, status, miles);
        todo.save();
        return todo;
    }

    public void setText(String todoText) {
        this.todoText = todoText;
    }

    public void setStatus(boolean status) {
        this.todoStatus = status;
    }

}
