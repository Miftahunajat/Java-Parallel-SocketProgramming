package com.miftahunajat.clustering;

import com.miftahunajat.util.Core;

import java.util.*;

public class HierarchicalClustering {

    public static int[] centroidLinkageClustering(double[][] data, int numberOfClusters){
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

            CentroidDistance minDistance = new CentroidDistance(Double.MAX_VALUE, -1,-1);
            for (int i = 0; i < data.length; i++) {
                if (mapData.get(i) == null) continue;

                for (int j = i+1; j < data.length; j++) {
                    if (mapData.get(j) == null) continue;


                    double distance = centroidLinkage(centroids[i], centroids[j]);
                    if (distance < minDistance.getDistance()){
                        minDistance = new CentroidDistance(distance,i,j);
                    }
                }
            }


            left = minDistance.getLeftCentroid();
            right = minDistance.getRightCentroid();

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
        return selCentroids;
    }

    private static double centroidLinkage(double[] centroid1, double[] centroid2) {
        double retval = 0;
        for (int i = 0; i < centroid1.length; i++) {
            retval += Math.pow((centroid1[i] - centroid2[i]),2);
        }
        return Math.sqrt(retval);
    }
}
