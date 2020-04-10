package com.clustering;

import java.util.concurrent.Callable;

public abstract class BatchFutureCentroidDistance implements Callable<Double[][]> {

    Double[][] dataRange1;
    Double[][] dataRange2;
    Double rangeI[],rangeJ[];

    public BatchFutureCentroidDistance(Double[][] dataRange1, Double[][] dataRange2, Double i[], Double j[]){
        this.dataRange1 = dataRange1;
        this.dataRange2 = dataRange2;
        this.rangeI = i;
        this.rangeJ = j;
    }

}

