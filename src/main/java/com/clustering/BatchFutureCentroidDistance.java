package com.clustering;

import com.util.VectorSpaceHelper;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class BatchFutureCentroidDistance implements Callable<Double[][]> {

    double[][] dataRange1;
    double[][] dataRange2;
    double rangeI[],rangeJ[];

    public BatchFutureCentroidDistance(double[][] dataRange1, double[][] dataRange2, double i[], double j[]){
        this.dataRange1 = dataRange1;
        this.dataRange2 = dataRange2;
        this.rangeI = i;
        this.rangeJ = j;
    }

    @Override
    public Double[][] call() throws Exception {
        double[][] substractsResult = VectorSpaceHelper.substractTwoMatrices(this.dataRange1, this.dataRange2);
        Double results[][] = new Double[substractsResult.length][];
        for (int j = 0; j < substractsResult.length; j++) {
            double res = 0;
            for (int k = 0; k < substractsResult[j].length; k++) {
                res += substractsResult[j][k]*substractsResult[j][k];
            }
            results[j] = new Double[]{res, rangeI[j], rangeJ[j]};
        }


        return results;
    }
}

