package com.miftahunajat.model;

import java.util.concurrent.Callable;

public abstract class ClusterAndVarianceFuture implements Callable<ClusterAndVariance> {
    protected int[] clusters;
    protected double[] variances;

    public ClusterAndVarianceFuture() {
    }
}
