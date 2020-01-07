package com.thread;

import java.util.concurrent.Callable;

public abstract class FutureDistanceMetric implements Callable<Double[]> {

    protected Double[][] mat1;

    public FutureDistanceMetric(Double[][] mat1){
        this.mat1 = mat1;

    }
}

