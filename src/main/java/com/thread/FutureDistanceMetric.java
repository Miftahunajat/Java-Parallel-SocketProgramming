package com.thread;

import java.util.concurrent.Callable;

public abstract class FutureDistanceMetric implements Callable<Double[][]> {

    protected double[][] mat1;
    protected double[][] mat2;

    public FutureDistanceMetric(double[][] mat1, double[][] mat2){
        this.mat1 = mat1;
        this.mat2 = mat2;

    }
}

