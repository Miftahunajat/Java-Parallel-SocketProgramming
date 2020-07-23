package com.miftahunajat.thread;

import java.util.concurrent.Callable;

public abstract class TaskFuture implements Callable<Double[][]> {

    protected String taskToSent;
    protected Double[][] mat1, mat2;

    public TaskFuture(String taskToSent){
        this.taskToSent  = taskToSent;
    }

    public TaskFuture(Double[][] mat1, Double[][] mat2){
        this.mat1 = mat1;
        this.mat2 = mat2;
    }
}

