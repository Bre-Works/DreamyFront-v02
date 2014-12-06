package com.breworks.dreamy.model;

import android.content.Context;
import android.util.Log;

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
    String notif;

    //build relationship
    String miles;


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

    public Todo(String name, boolean status, Milestone mil, Date taskDeadline, Boolean notification) {
        this.todoText = name;
        this.todoStatus = status;
        this.miles = mil.getId().toString();
        this.taskdeadline = taskDeadline;
        this.notif = String.valueOf(notification);
        this.save();
    }



    public String getText() {
        return this.todoText;
    }

    public boolean getStatus() {
        return this.todoStatus;
    }

    public static Todo createTodo(String name, boolean status, Milestone miles, Date taskDeadline, Boolean notification) {
        Todo todo = new Todo(name, status, miles, taskDeadline, notification);
        todo.save();
        return todo;
    }

    public void setText(String todoText) {
        this.todoText = todoText; this.save();
    }

    public void setNotifStatus(Boolean notification){
        this.notif = String.valueOf(notification);
        Log.e("Notif value", String.valueOf(notification));
        this.save();
    }


    public void setStatus(boolean status) {
        this.todoStatus = status; this.save();
    }

    public static List<Todo> searchByMilestone(Milestone mil) {
        List<Todo> todo = Todo.findWithQuery(Todo.class, "Select * from Todo Where miles = ?", mil.getId().toString());
        return todo;
    }

    public static List<Todo> searchByNotifStatus(Boolean nStat){
        List<Todo> todo = Todo.findWithQuery(Todo.class, "Select * from Todo Where notif = ?", String.valueOf(nStat));
        return todo;
    }

    public Date getDeadline(Todo t){
        return t.taskdeadline;
    }

    public String getTask(Todo t){
        return t.todoText;
    }

    public String getNotifStatus(Todo t){
        return t.notif;
    }

    public static Todo findByTaskID(Long currentID){
        Todo td = Todo.findById(Todo.class, currentID);
        return td;
    }




    public static Todo saveDeadline(Date taskDeadline, Todo td){
        Log.e("TASK DEADLINE FROM TDDETAIL", String.valueOf(taskDeadline));
        Log.e("The task", td.todoText);
        Log.e("task Deadline save deadline", String.valueOf(td.taskdeadline));
        td.taskdeadline = taskDeadline;

        td.save();
        return td;
    }

}
