package com.miftahunajat.thread;

import java.util.concurrent.Callable;

public abstract class FutureDistanceMetric implements Callable<Double[][]> {

    protected Double[][] mat1;
    protected Double[] rangeI;
    protected Double[][] mat2;
    protected Double[] rangeJ;


    public FutureDistanceMetric(Double[][] mat1, Double[][] mat2, Double[] rangeI, Double[] rangeJ){
        this.mat1 = mat1;
        this.mat2 = mat2;
        this.rangeI = rangeI;
        this.rangeJ = rangeJ;
    }
}

