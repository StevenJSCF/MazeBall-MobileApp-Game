package com.cs309.tutorial.tests;

import java.util.Date;

public class Task {

    private String ID;
    private String title;
    private String description;
    private String  dueDate;
    private String completed;

    public Task(String ID, String title, String description, String dueDate, String completed) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public String getTitle(){
        return this.title = title;
    }

    public String getID(){
        return this.ID = ID;
    }

    public String getDescription(){
        return this.description = description;
    }
    public String getDueDate(){
        return this.dueDate = dueDate;
    }
    public String getCompleted(){
        return this.completed = completed;
    }



    @Override
    public String toString() {
        return ID + " "
                + title + " "
                + description + " "
                + dueDate + " "
                + completed ;
    }










}





