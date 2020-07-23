package com.miftahunajat.clustering;

import com.miftahunajat.thread.MultiThreadManager;
import com.miftahunajat.util.Core;
import com.miftahunajat.util.VectorSpaceHelper;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;

public class SerialHierarchicalClustering {

    public static int[] centroidLinkageClustering(Double[][] data, int numberOfClusters) throws InterruptedException, IOException {
        int currentClusterCount = data.length;
        int[] selCentroids = new int[data.length];
        Double[][] centroids = new Double[data.length][];

        Map<Integer, Double[][]> mapData = new HashMap<>();

        for (int i = 0; i < data.length; i++) {
            mapData.put(i, new Double[][]{data[i]});
            selCentroids[i] = i;
            centroids[i] = data[i].clone();
        }
        while (currentClusterCount != numberOfClusters){
            int left;
            int right;

            CentroidDistance minDistance = new CentroidDistance(Double.MAX_VALUE, -1, -1);

            for (int i = 0; i < data.length; i++) {
                if (mapData.get(i) == null) continue;

                for (int j = i+1; j < data.length; j++) {
                    if (mapData.get(j) == null) continue;
                    double distance = VectorSpaceHelper.getDistances(centroids[i], centroids[j]);
                    if (distance < minDistance.getDistance()){
                        minDistance = new CentroidDistance(distance, i, j);
                    }

                }
            }


            left = minDistance.getLeftCentroid();
            right = minDistance.getRightCentroid();
//
            int finalLeft = left;
            int finalRight = right;
            if (minDistance.getLeftCentroid() < minDistance.getRightCentroid()){
                Double[][] newData = Core.joinMultipleArray(mapData.get(left), mapData.get(right));
                mapData.remove(right);
                mapData.put(left, newData);
                selCentroids = Arrays
                        .stream(selCentroids).map(idx -> idx == finalRight ? finalLeft : idx)
                        .toArray();
                centroids[left] = Core.getCentroidsFromDouble(newData);
            }else{
                Double[][] newData = Core.joinMultipleArray(mapData.get(right), mapData.get(left));
                mapData.remove(left);
                mapData.put(right, newData);
                selCentroids = Arrays
                        .stream(selCentroids).map(idx -> idx == finalLeft ? finalRight : idx)
                        .toArray();
                centroids[right] = Core.getCentroidsFromDouble(newData);
            }

            currentClusterCount--;

        }
        return selCentroids;
    }

    public static int[] pCentroidLinkageClustering(double[][] data, int numberOfClusters) {
        int currentClusterCount = data.length;
        int[] selCentroids = new int[data.length];
        double[][] centroids = new double[data.length][];

        Map<Integer, double[][]> mapData = new HashMap<>();

        for (int i = 0; i < data.length; i++) {
            mapData.put(i, new double[][]{data[i]});
            selCentroids[i] = i;
            centroids[i] = data[i].clone();
        }
        while (currentClusterCount != numberOfClusters){
            int left;
            int right;

            CentroidDistance minDistance = new CentroidDistance(Double.MAX_VALUE, -1, -1);

            for (int i = 0; i < data.length; i++) {
                if (mapData.get(i) == null) continue;

                for (int j = i+1; j < data.length; j++) {
                    if (mapData.get(j) == null) continue;
                    double distance = VectorSpaceHelper.getDistances(centroids[i], centroids[j]);
                    if (distance < minDistance.getDistance()){
                        minDistance = new CentroidDistance(distance, i, j);
                    }

                }
            }


            left = minDistance.getLeftCentroid();
            right = minDistance.getRightCentroid();
//
            int finalLeft = left;
            int finalRight = right;
            if (minDistance.getLeftCentroid() < minDistance.getRightCentroid()){
                double[][] newData = Core.joinMultipleArray(mapData.get(left), mapData.get(right));
                mapData.remove(right);
                mapData.put(left, newData);
                selCentroids = Arrays
                        .stream(selCentroids).map(idx -> idx == finalRight ? finalLeft : idx)
                        .toArray();
                centroids[left] = Core.getCentroidsFromDouble(newData);
            }else{
                double[][] newData = Core.joinMultipleArray(mapData.get(right), mapData.get(left));
                mapData.remove(left);
                mapData.put(right, newData);
                selCentroids = Arrays
                        .stream(selCentroids).map(idx -> idx == finalLeft ? finalRight : idx)
                        .toArray();
                centroids[right] = Core.getCentroidsFromDouble(newData);
            }

            currentClusterCount--;

        }
        return normalize(selCentroids);
    }

    private static int[] normalize(int[] selCentroids) {
        Map<Integer, Integer> keyVal = new HashMap<>();
        int counter = -1;
        for (int i = 0; i < selCentroids.length; i++) {
            if (keyVal.containsKey(selCentroids[i]))
                selCentroids[i] = keyVal.get(selCentroids[i]);
            else{
                counter++;
                keyVal.put(selCentroids[i], counter);
                selCentroids[i] = counter;
            }

        }
        return selCentroids;
    }

    private static double centroidLinkage(double[] centroid1, double[] centroid2) {
        double retval = 0;
        for (int i = 0; i < centroid1.length; i++) {
            retval += Math.pow((centroid1[i] - centroid2[i]),2);
        }
        return Math.sqrt(retval);
    }

    private static Future<Double> centroidLinkage(Double[][] data1, Double[][] data2, MultiThreadManager mtm) {
        Double[] centroid1 = Core.getCentroidsFromDouble(data1);
        Double[] centroid2 = Core.getCentroidsFromDouble(data2);
        return mtm.getDistance(centroid1, centroid2);
    }

    public int[] SingleLinkage(String[][] data, int NumberOfCluster){
        int cluster[] = null;
        return cluster;
    }

    public int[] CentroidLinkage(String[][] data, int NumberOfCluster){
        int cluster[] = null;
        return cluster;
    }

    public int[] CompleteLinkage(String[][] data, int NumberOfCluster){
        int cluster[] = null;
        return cluster;
    }

    public int[] AverageLinkage(String[][] data, int NumberOfCluster){
        int cluster[] = null;
        return cluster;
    }

    public double[] getCentroid(double[][] data) {
        double[] centroid = new double[data[0].length];
        for(int i = 0; i < data[0].length; ++i) {
            double initCentroid = 0.0;
            for(int j = 0; j < data.length; ++j) {
                initCentroid = initCentroid + data[j][i];
            }
            centroid[i] = initCentroid / (double)data.length;
        }
        return centroid;
    }
}
