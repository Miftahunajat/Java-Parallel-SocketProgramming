package com;

public abstract class Task implements Runnable {

    protected String taskToSent;
    protected double[][] mat1, mat2;

    public Task(String taskToSent){
        this.taskToSent  = taskToSent;
    }

    public Task(double[][] mat1, double[][] mat2){
        this.mat1 = mat1;
        this.mat2 = mat2;
    }
}

