package com.thread;

import java.util.concurrent.Callable;

public abstract class GetDistanceFuture implements Callable<Double> {

    protected Double[] mat1, mat2;

    public GetDistanceFuture(Double[] mat1, Double[] mat2){
        this.mat1 = mat1;
        this.mat2 = mat2;
    }
}

