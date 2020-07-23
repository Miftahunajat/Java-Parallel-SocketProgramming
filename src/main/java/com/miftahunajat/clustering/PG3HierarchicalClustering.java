package com.miftahunajat.clustering;

import com.miftahunajat.Config;
import com.miftahunajat.thread.MultiThreadManager;
import com.miftahunajat.tunnel.ServerConfig;
import com.miftahunajat.util.Core;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class PG3HierarchicalClustering {

    public static int[] centroidLinkageClustering(Double[][] data, int numberOfClusters, ServerConfig serverConfig) throws InterruptedException, IOException {
        ExecutorService ex = Executors.newFixedThreadPool(1);
        MultiThreadManager mtm = MultiThreadManager.getInstance(serverConfig);
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
}
