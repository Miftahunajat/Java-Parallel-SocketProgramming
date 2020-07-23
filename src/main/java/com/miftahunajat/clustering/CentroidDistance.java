package com.miftahunajat.clustering;

public class CentroidDistance {
    private double distance;
    private int leftCentroid;
    private int rightCentroid;

    public CentroidDistance(double distance, int leftCentroid, int rightCentroid) {
        this.distance = distance;
        this.leftCentroid = leftCentroid;
        this.rightCentroid = rightCentroid;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getLeftCentroid() {
        return leftCentroid;
    }

    public void setLeftCentroid(int leftCentroid) {
        this.leftCentroid = leftCentroid;
    }

    public int getRightCentroid() {
        return rightCentroid;
    }

    public void setRightCentroid(int rightCentroid) {
        this.rightCentroid = rightCentroid;
    }
}
