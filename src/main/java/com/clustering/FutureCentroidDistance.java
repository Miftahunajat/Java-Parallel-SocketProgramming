package com.clustering;

import java.util.concurrent.Callable;

public abstract class FutureCentroidDistance implements Callable<CentroidDistance> {

    protected  Double[] centroid1;
    protected  Double[] centroid2;
    protected int left, right;

    public FutureCentroidDistance(Double[] centroid1, Double[] centroid2, int i, int j){
        this.centroid1 = centroid1;
        this.centroid2 = centroid2;
        this.left = i;
        this.right = j;
    }
}

