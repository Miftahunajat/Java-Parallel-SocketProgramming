package com.miftahunajat.thread;

import java.util.concurrent.Callable;

public abstract class FutureGetDistance implements Callable<Double> {

    protected Double[] mat1, mat2;

    public FutureGetDistance(Double[] mat1, Double[] mat2){
        this.mat1 = mat1;
        this.mat2 = mat2;
    }
}

