package com.breworks.dreamy.model;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

/**
 * Created by Ryan on 05/10/2014.
 */
public class Todo extends SugarRecord<Todo> {

    String todoText;
    boolean todoStatus;
    Date taskdeadline;

    //build relationship
    String miles;

    // constructors
    public Todo() {
    }

    public Todo(String text, boolean status, Milestone mil, Date taskDeadline){
        this.todoText = text;
        this.todoStatus = status;
        this.miles = mil.getId().toString();
        this.taskdeadline = taskDeadline;
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
        this.miles = mil.getId().toString();
        this.save();
    }



    public String getText() {
        return this.todoText;
    }

    public boolean getStatus() {
        return this.todoStatus;
    }

    public static Todo createTodo(String name, boolean status, Milestone miles) {
        Todo todo = new Todo(name, status, miles);
        todo.save();
        return todo;
    }

    public void setText(String todoText) {
        this.todoText = todoText; this.save();
    }

    public void setStatus(boolean status) {
        this.todoStatus = status; this.save();
    }

    public static List<Todo> searchByMilestone(Milestone mil) {
        List<Todo> todo = Todo.findWithQuery(Todo.class, "Select * from Todo Where miles = ?", mil.getId().toString());
        return todo;
    }

    public void saveDeadline(Date taskDeadline, long currentID){
        Todo td = Todo.findById(Todo.class, currentID);
        td.taskdeadline = taskdeadline;
        td.save();
    }

}
