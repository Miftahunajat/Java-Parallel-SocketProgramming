package com.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public abstract class TaskFuture implements Callable<Double[][]> {

    protected String taskToSent;
    protected double[][] mat1, mat2;

    public TaskFuture(String taskToSent){
        this.taskToSent  = taskToSent;
    }

    public TaskFuture(double[][] mat1, double[][] mat2){
        this.mat1 = mat1;
        this.mat2 = mat2;
    }
}

