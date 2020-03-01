package com.model;

public class ClusterAndVariance {
    private int[] clusters;
    private double[] variances;

    public ClusterAndVariance(int[] clusters, double[] variances) {
        this.clusters = clusters;
        this.variances = variances;
    }

    public int[] getClusters() {
        return clusters;
    }

    public void setClusters(int[] clusters) {
        this.clusters = clusters;
    }

    public double[] getVariances() {
        return variances;
    }

    public void setVariances(double[] variances) {
        this.variances = variances;
    }
}
