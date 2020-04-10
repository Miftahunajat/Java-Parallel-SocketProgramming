package com.clustering;

import com.Config;
import com.thread.MultiThreadManager;
import com.util.Core;
import com.util.Distance;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class PG3HierarchicalClustering {

    public static int[] centroidLinkageClustering(Double[][] data, int numberOfClusters) throws InterruptedException, IOException {
        ExecutorService ex = Executors.newFixedThreadPool(1);
        MultiThreadManager mtm = MultiThreadManager.getInstance();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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

//            final CentroidDistance[] minDistance = {new CentroidDistance(Double.MAX_VALUE, -1, -1)};
            List<Future<CentroidDistance>> futureCentroidDistances = new ArrayList<>();

            for (int i = 0; i < data.length; i++) {
                if (mapData.get(i) == null) continue;

                for (int j = i+1; j < data.length; j++) {
                    if (mapData.get(j) == null) continue;

                    if (mapData.size() > Config.MAX_LIMIT) {
                        futureCentroidDistances.add(mtm.getCentroidDistance(centroids[i], centroids[j], i, j));
                    }else{
                        futureCentroidDistances.add(mtm.getCentroidDistanceMain(centroids[i], centroids[j], i, j));
                    }
                }
            }

//            double[] distanceMin = new double[]{Double.MAX_VALUE, -1, -1};
            final CentroidDistance[] distanceMin = {new CentroidDistance(Double.MAX_VALUE, -1, -1)};
            futureCentroidDistances.forEach(result -> {
                try {
                    CentroidDistance res = result.get();
                    if (res.getDistance() < distanceMin[0].getDistance()) {
                        distanceMin[0] = res;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });


            left = distanceMin[0].getLeftCentroid();
            right = distanceMin[0].getRightCentroid();

            int finalLeft = left;
            int finalRight = right;
            if (distanceMin[0].getLeftCentroid() < distanceMin[0].getRightCentroid()){
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
        executorService.shutdownNow();
        mtm.close();
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

    public int[] optimizedCentroidLinkage(double[][] data, int numberOfCluster){
        int[] clusters = new int[data.length];
        double[][] centroids = new double[data.length][data[0].length];
        int currentClusters = data.length;
        for(int i = 0; i < data.length; i++) {
            clusters[i] = i;
            centroids[i] = data[i];
        }

        do {
            double minDistance = Double.MAX_VALUE;
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data.length; j++) {
                    if (centroids[i] == centroids[j]) continue;
                    double distance = Distance.getDistance(centroids[i], centroids[j] );
                }
            }
             currentClusters--;
        }while (currentClusters < numberOfCluster);
//        new Vector().getDistance()
        return new int[]{0};
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
