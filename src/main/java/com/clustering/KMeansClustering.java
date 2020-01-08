package com.clustering;


import com.util.Core;

import java.lang.reflect.Array;
import java.util.Arrays;

public class KMeansClustering {

//    public int[] KMeans(double[][] data, int NumberOfCluster, double[][] centroids) {
//        int[] clusters = new int[data.length];
//        double[][] newCentroids = new com.bayudwiyansatria.mat.Array().initArray(NumberOfCluster, data[0].length, 0.0);
//
//        double[][] currentCentroids = new com.bayudwiyansatria.mat.Array().initArray(NumberOfCluster, data[0].length, 0.0);
//        double[] mins = new double[data[0].length];
//        double[] maxs = new double[data[0].length];
//
////        Count The Maxs and min
//        for (int i = 0; i < data[0].length; i++) {
//            for (int j = 0; j < data.length; j++) {
//
//            }
//        }
//
////        for (int i = 0; i < NumberOfCluster; i++) {
////            for (int j = 0; j < data[0].length; j++) {
////                currentCentroids[i][j] =
////            }
////            currentCentroids[i]
////        }
//        double[] nCentroids = new com.bayudwiyansatria.mat.Array().initArray(NumberOfCluster, 0.0);
//        boolean cluster;
//        do {
//            for(int i = 0; i < data.length; ++i) {
//                int minValue = 0;
//                double maxValue = 1.7976931348623157E308;
//                for(int j = 0; j < NumberOfCluster; ++j) {
//                    double distanceMetric = 0.0;
//                    for(int k = 0; k < data[0].length; ++k) {
//                        double distance = data[i][k] - centroids[j][k];
//                        distanceMetric += distance * distance;
//                    }
//                    if (distanceMetric < maxValue) {
//                        minValue = j;
//                        maxValue = distanceMetric;
//                    }
//                }
//                clusters[i] = minValue;
//                nCentroids[minValue]++;
//                for(int k = 0; k < data[0].length; ++k) {
//                    newCentroids[minValue][k] += data[i][k];
//                }
//            }
//            cluster = true;
//            for(int i = 0; i < NumberOfCluster; ++i) {
//                for(int j = 0; j < data[0].length; ++j) {
//                    newCentroids[i][j] /= nCentroids[i];
//                    if (Math.abs(centroids[i][j] - newCentroids[i][j]) > 0.0001) {
//                        cluster = false;
//                    }
//                }
//            }
//            centroids = new com.bayudwiyansatria.mat.Array().copyArray(newCentroids);
//            newCentroids = new com.bayudwiyansatria.mat.Array().initArray(NumberOfCluster, data[0].length, 0.0);
//            nCentroids = new com.bayudwiyansatria.mat.Array().initArray(NumberOfCluster, 0.0);
//        } while(!cluster);
//
//        return clusters;
//    }


    public static int[] KMeans(double[][] data, int numberOfCluster) {
        int loop = 0;
        int n = data.length;
        double[][] dataT = new com.bayudwiyansatria.mat.Mat().transposeMatrix(data);
        double[] jarakMin = new double[n];
        Arrays.fill(jarakMin, Double.MAX_VALUE);
        int[] selCentroids = new int[n];

        double[][] centroids = new double[numberOfCluster][data[0].length];
        for (int i = 0; i < data[0].length; i++) {
            double max = Arrays.stream(dataT[i]).max().getAsDouble();
            double min = Arrays.stream(dataT[i]).min().getAsDouble();
            for (int j = 0; j < centroids.length; j++) {
                double range = (max - min) + 1;
               centroids[j][i] = Math.random() * range + min;
            }
        }

        boolean same = false;
        while (!same){
            loop++;
            System.out.println(Arrays.deepToString(centroids));
            same = true;
            double[][] distanceDataCentroids = new double[centroids.length][data.length];
            for (int i = 0; i < centroids.length; i++) {
                for (int j = 0; j < data.length; j++) {
                    distanceDataCentroids[i][j] = getDistance(centroids[i], data[j]);
                }
            }
            for (int i = 0; i < centroids.length; i++) {
                for (int j = 0; j < data.length; j++) {
                    double jarak = distanceDataCentroids[i][j];
                    if (jarak < jarakMin[j]){
                        selCentroids[j] = i;
                        jarakMin[j] = jarak;
                    }
                }
            }


            double[][] newCentroids = Core.deepCopyIntMatrix(centroids);
            for (int i = 0; i < newCentroids.length; i++) {
                for (int j = 0; j < centroids[i].length; j++) {

                    double avg = 0.0;
                    int counter = 0;
                    for (int k = 0; k < data.length; k++) {
                        avg += selCentroids[k] == i ? data[k][j] : 0;
                        counter += selCentroids[k] == i ? 1 : 0;
                    }
                    avg/=counter;

                    newCentroids[i][j] = avg;
                }
            }
//            Arrays.fill(centroids, new double[]{0,0,0});


            System.out.println(Arrays.deepToString(centroids));
            System.out.println(Arrays.deepToString(newCentroids));

            if (!Arrays.deepEquals(newCentroids, centroids)) same = false;
            centroids = Core.deepCopyIntMatrix(newCentroids);
            System.out.println(loop);
        }
        return selCentroids;
    }


    public static int[] parallelKMeans(double[][] data, int numberOfCluster) {
        int loop = 0;
        int n = data.length;
        double[][] dataT = new com.bayudwiyansatria.mat.Mat().transposeMatrix(data);
        double[] jarakMin = new double[n];
        Arrays.fill(jarakMin, Double.MAX_VALUE);
        int[] selCentroids = new int[n];

        double[][] centroids = new double[numberOfCluster][data[0].length];
        for (int i = 0; i < data[0].length; i++) {
            double max = Arrays.stream(dataT[i]).max().getAsDouble();
            double min = Arrays.stream(dataT[i]).min().getAsDouble();
            for (int j = 0; j < centroids.length; j++) {
                double range = (max - min) + 1;
                centroids[j][i] = Math.random() * range + min;
            }
        }

        boolean same = false;
        while (!same){
            loop++;
            System.out.println(Arrays.deepToString(centroids));
            same = true;
            double[][] distanceDataCentroids = new double[centroids.length][data.length];
            for (int i = 0; i < centroids.length; i++) {
                for (int j = 0; j < data.length; j++) {
                    distanceDataCentroids[i][j] = getDistance(centroids[i], data[j]);
                }
            }
            for (int i = 0; i < centroids.length; i++) {
                for (int j = 0; j < data.length; j++) {
                    double jarak = distanceDataCentroids[i][j];
                    if (jarak < jarakMin[j]){
                        selCentroids[j] = i;
                        jarakMin[j] = jarak;
                    }
                }
            }


            double[][] newCentroids = Core.deepCopyIntMatrix(centroids);
            for (int i = 0; i < newCentroids.length; i++) {
                for (int j = 0; j < centroids[i].length; j++) {

                    double avg = 0.0;
                    int counter = 0;
                    for (int k = 0; k < data.length; k++) {
                        avg += selCentroids[k] == i ? data[k][j] : 0;
                        counter += selCentroids[k] == i ? 1 : 0;
                    }
                    avg/=counter;

                    newCentroids[i][j] = avg;
                }
            }
//            Arrays.fill(centroids, new double[]{0,0,0});


            System.out.println(Arrays.deepToString(centroids));
            System.out.println(Arrays.deepToString(newCentroids));

            if (!Arrays.deepEquals(newCentroids, centroids)) same = false;
            centroids = Core.deepCopyIntMatrix(newCentroids);
            System.out.println(loop);
        }
        return selCentroids;
    }






    private static double getDistance(double[] centroid, double[] datum) {
//        if (centroid.length != datum.length) throw new Exception("error panjang tidak sama");
        double result = 0;
        for (int i = 0; i < datum.length; i++) {
            result += Math.pow(Math.abs(centroid[i] - datum[i]), 2);
        }
        return Math.sqrt(result);
    }

//    private double[] getDistanceCentroids(double[] centroid, double[][] data, int j, double[] jarakMin) {
//        double[] retval = new double[data.length];
//        for (int i = 0; i < data.length; i++) {
//            retval[i] = getDistance(centroid, data[i]);
//            if (retval[i] > jarakMin[i]){
//
//            }
//        }
//        return new double[0];
//    }
}
