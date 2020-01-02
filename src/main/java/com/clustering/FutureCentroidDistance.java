package com.clustering;

import java.util.concurrent.Callable;

public abstract class FutureCentroidDistance implements Callable<CentroidDistance> {

    double[] centroid1;
    double[] centroid2;
    int i,j;

    public FutureCentroidDistance(double[] centroid1, double[] centroid2, int i, int j){
        this.centroid1 = centroid1;
        this.centroid2 = centroid2;
        this.i = i;
        this.j = j;
    }
}

