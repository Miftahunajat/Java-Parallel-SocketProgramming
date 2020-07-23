package com.miftahunajat;

public abstract class Task implements Runnable {

    protected String taskToSent;
    protected Double[][] mat1, mat2;

    public Task(String taskToSent){
        this.taskToSent  = taskToSent;
    }

    public Task(Double[][] mat1, Double[][] mat2){
        this.mat1 = mat1;
        this.mat2 = mat2;
    }
}

