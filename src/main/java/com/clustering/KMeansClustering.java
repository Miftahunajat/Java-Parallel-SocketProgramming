package com.clustering;

import java.util.Arrays;
import java.util.Collections;

public class KMeansClustering {

    public int[] KMeans(double[][] data, int NumberOfCluster, double[][] centroids) {
        int[] clusters = new int[data.length];
        double[][] newCentroids = new com.bayudwiyansatria.mat.Array().initArray(NumberOfCluster, data[0].length, 0.0);

        double[][] currentCentroids = new com.bayudwiyansatria.mat.Array().initArray(NumberOfCluster, data[0].length, 0.0);
        double[] mins = new double[data[0].length];
        double[] maxs = new double[data[0].length];

//        Count The Maxs and min
        for (int i = 0; i < data[0].length; i++) {
            for (int j = 0; j < data.length; j++) {

            }
        }

//        for (int i = 0; i < NumberOfCluster; i++) {
//            for (int j = 0; j < data[0].length; j++) {
//                currentCentroids[i][j] =
//            }
//            currentCentroids[i]
//        }
        double[] nCentroids = new com.bayudwiyansatria.mat.Array().initArray(NumberOfCluster, 0.0);
        boolean cluster;
        do {
            for(int i = 0; i < data.length; ++i) {
                int minValue = 0;
                double maxValue = 1.7976931348623157E308;
                for(int j = 0; j < NumberOfCluster; ++j) {
                    double distanceMetric = 0.0;
                    for(int k = 0; k < data[0].length; ++k) {
                        double distance = data[i][k] - centroids[j][k];
                        distanceMetric += distance * distance;
                    }
                    if (distanceMetric < maxValue) {
                        minValue = j;
                        maxValue = distanceMetric;
                    }
                }
                clusters[i] = minValue;
                nCentroids[minValue]++;
                for(int k = 0; k < data[0].length; ++k) {
                    newCentroids[minValue][k] += data[i][k];
                }
            }
            cluster = true;
            for(int i = 0; i < NumberOfCluster; ++i) {
                for(int j = 0; j < data[0].length; ++j) {
                    newCentroids[i][j] /= nCentroids[i];
                    if (Math.abs(centroids[i][j] - newCentroids[i][j]) > 0.0001) {
                        cluster = false;
                    }
                }
            }
            centroids = new com.bayudwiyansatria.mat.Array().copyArray(newCentroids);
            newCentroids = new com.bayudwiyansatria.mat.Array().initArray(NumberOfCluster, data[0].length, 0.0);
            nCentroids = new com.bayudwiyansatria.mat.Array().initArray(NumberOfCluster, 0.0);
        } while(!cluster);

        return clusters;
    }
}
