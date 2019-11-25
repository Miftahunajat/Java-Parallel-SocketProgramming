package com;

public abstract class Task implements Runnable {

    protected String taskToSent;

    public Task(String taskToSent){
        this.taskToSent  = taskToSent;
    }
}

