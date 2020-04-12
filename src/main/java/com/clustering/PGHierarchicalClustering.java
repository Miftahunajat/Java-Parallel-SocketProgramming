package com.clustering;

import com.thread.MultiThreadManager;
import com.tunnel.ServerConfig;
import com.util.Core;

import java.util.*;
import java.util.concurrent.*;

public class PGHierarchicalClustering {

    public static int[] centroidLinkageClustering(Double[][] data, int numberOfClusters, ServerConfig serverConfig) throws Exception {
        MultiThreadManager mtm = MultiThreadManager.getInstance(serverConfig);
        int currentClusterCount = data.length;

        int[] selCentroids = new int[data.length];
        Double[][] centroids = new Double[data.length][];

        Map<Integer, Double[][]> mapData = new HashMap<>();

        for (int i = 0; i < data.length; i++) {
            mapData.put(i, new Double[][]{data[i]});
            selCentroids[i] = i;
            centroids[i] = data[i].clone();
        }
        int left;
        int right;
        right = currentClusterCount;
        left = right;

        while (currentClusterCount != numberOfClusters){
            List<Future<Double[][]>> futureCentroidDistances = new ArrayList<>();
            int number = currentClusterCount;
            for (int i = 0; i < data.length; i++) {
                if (mapData.get(i) == null) {
                    number++;
                    continue;
                }

                Double rangeI[] = new Double[number - 1-i];
                Double dataCentroidsI[][] = new Double[number - 1 - i][];
                Double rangeJ[] = new Double[number - 1-i];
                Double dataCentroidsJ[][] = new Double[number - 1 - i][];
                int idx = 0;
                for (int j = i+1; j < data.length; j++) {
                    if (mapData.get(j) == null) continue;
                    rangeJ[idx] = (double) j;
                    dataCentroidsJ[idx] = centroids[j].clone();
                    rangeI[idx] = (double) i;
                    dataCentroidsI[idx] = centroids[i].clone();
                    idx++;
                }


                if (dataCentroidsI.length == 0) continue;

                futureCentroidDistances.add(mtm.getDistanceMetric(
                        dataCentroidsI, dataCentroidsJ,
                        rangeI, rangeJ
                ));
            }
            Double[] distanceMin = new Double[]{Double.MAX_VALUE, -1.0,-1.0};
            futureCentroidDistances.stream().forEach( result ->{
                try {
                    Double[][] res = result.get();
                    Arrays.stream(res).forEach(k->{
                        if (k[0] < distanceMin[0]){
                            distanceMin[0] = k[0];
                            distanceMin[1] = k[1];
                            distanceMin[2] = k[2];
                        }
                    });

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });

//            executorService.invokeAll(futureCentroidDistances);
//            futureCentroidDistances.stream().forEach(futureCentroidDistanceFuture -> {
//                        try {
//                            futureCentroidDistanceFuture.get();
//                        } catch (InterruptedException | ExecutionException e) {
//                            e.printStackTrace();
//                        }
//                    }
//            );
//


//            left = minDistance[0].getLeftCentroid();
//            right = minDistance[0].getRightCentroid();

            left = distanceMin[1].intValue();
            right = distanceMin[2].intValue();

            int finalLeft = left;
            int finalRight = right;
            if (left < right){
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
//        executorService.shutdownNow();
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
