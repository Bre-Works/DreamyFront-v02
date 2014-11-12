package com.breworks.dreamy.model;

import com.breworks.dreamy.ToDoList;
import com.orm.SugarRecord;

/**
 * Created by Ryan on 05/10/2014.
 */
public class Todo extends SugarRecord<Todo> {

    String todoText;
    boolean todoStatus;
    int day;
    int month;
    int year;
    int hour;
    int minute;

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

    public Todo(String text, Boolean status) {
        this.todoText = text;
        this.todoStatus = status;
        this.save();
    }

    public Todo(String name, boolean status, Milestone mil) {
        this.todoText = name;
        this.todoStatus = status;
        this.miles = mil;
        this.save();
    }

    public Todo(int day, int month, int year, int hour, int minute){
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
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

    public static Todo addDeadline(int day, int month, int year, int hour, int minute){
        Todo t = new Todo();
        t.findById(Todo.class, t.getCurrentTaskID());
        t.day = day;
        t.month = month;
        t.year = year;
        t.hour = hour;
        t.minute = minute;
        t.save();
        return t;
    }

    public long getCurrentTaskID(){
        ToDoList tl = new ToDoList();
        return tl.getCurrentID();
    }


    public void setText(String todoText) {
        this.todoText = todoText;
    }

    public void setStatus(boolean status) {
        this.todoStatus = status;
    }
}