package com.thread;

import java.util.concurrent.Callable;

public abstract class FutureDistanceMetric implements Callable<Double[]> {

    protected Double[][] mat1;
    protected Double[][] mat2;

    public FutureDistanceMetric(Double[][] mat1, Double[][] mat2){
        this.mat1 = mat1;
        this.mat2 = mat2;

    }
}

