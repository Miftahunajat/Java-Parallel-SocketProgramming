package com.thread;

import com.clustering.CentroidDistance;

import java.util.concurrent.Callable;

public abstract class FutureGetCentroidDistance implements Callable<CentroidDistance> {

    protected Double[] mat1, mat2;
    protected int i, j;

    public FutureGetCentroidDistance(Double[] mat1, Double[] mat2, int i, int j){
        this.mat1 = mat1;
        this.mat2 = mat2;
        this.i = i;
        this.j = j;
    }
}

