package com.clustering;

public class DataIndex {
    private int index;
    private double[] data;

    public DataIndex(int index, double[] data) {
        this.index = index;
        this.data = data;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }
}
