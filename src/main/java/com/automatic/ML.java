package com.automatic;

import com.samples.Clustering;
import com.util.Distance;
import com.util.MathUtil;
import com.util.Utils;

import java.util.Arrays;

public class ML {

    public double[] getVariance(double[][] data, int[] clusters) {
        int[] unique = Utils.getUnique(clusters);
        double[] Variance = new double[2];
        Arrays.fill(Variance, 0,2,0.0);
        double[] variance = new double[unique.length];
        double[][] centroid = new double[unique.length][data[0].length];
        int[] newUnique = new int[unique.length];
        double tmpl = 0.0;
        int[][] position = new int[unique.length][data.length];
        int[] initCentroid = new int[unique.length];
        Arrays.fill(initCentroid, 0, unique.length, 0);
        if (unique.length > 1) {
            int i;
            for(i = 0; i < data.length; ++i) {
                int cluster = clusters[i];
                position[cluster][initCentroid[cluster]] = i;
                initCentroid[cluster]++;
            }

            double distanceVector;
            double distance;
            for(i = 0; i < unique.length; ++i) {
                newUnique[i] = initCentroid[i];
                double[] tmp = Utils.initArray(data[0].length, 0.0);

                int j;
                for(j = 0; j < newUnique[i]; ++j) {
                    for(int t = 0; t < data[0].length; ++t) {
                        tmp[t] = tmp[t] + data[position[i][j]][t];
                    }
                }

                centroid[i] = MathUtil.division(tmp, newUnique[i]);
                if (newUnique[i] == 1) {
                    variance[i] = 0.0;
                } else {
                    distanceVector = 0.0;

                    for(j = 0; j < newUnique[i]; ++j) {

                        distance = Distance.getDistance(data[position[i][j]], centroid[i]);
                        distanceVector += distance * distance;
                    }

                    variance[i] = distanceVector;
                }

                tmpl += variance[i];
            }

            double varianceWithin = tmpl / (double)(data.length - unique.length);
            distanceVector = 0.0;
            double[] grandMean = Utils.getCentroid(centroid);

            for(i = 0; i < unique.length; ++i) {
                distance = Distance.getDistance(centroid[i], grandMean);
                distanceVector += (double)newUnique[i] * distance * distance;
            }

            double varianceBetween = distanceVector / (double)(unique.length - 1);
            Variance[0] = varianceWithin;
            Variance[1] = varianceBetween;
        }

        return Variance;
    }
}