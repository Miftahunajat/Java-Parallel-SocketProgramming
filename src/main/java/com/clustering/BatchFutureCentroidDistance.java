package com.clustering;

import com.util.VectorSpaceHelper;

import java.util.Arrays;
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

//    @Override
//    public Double[][] call() throws Exception {
//        Double results[][] = new Double[substractsResult.length][];
//        for (int j = 0; j < substractsResult.length; j++) {
//            double res = 0;
//            for (int k = 0; k < substractsResult[j].length; k++) {
//                res += substractsResult[j][k]*substractsResult[j][k];
//            }
//            results[j] = new Double[]{res, rangeI[j], rangeJ[j]};
//        }
//
//
//        return results;
//    }
}

