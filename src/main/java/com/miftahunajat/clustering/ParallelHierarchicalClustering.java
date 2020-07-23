package com.miftahunajat.clustering;

import com.miftahunajat.thread.MultiThreadManager;
import com.miftahunajat.tunnel.ServerConfig;
import com.miftahunajat.util.Core;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class ParallelHierarchicalClustering {

    public static int[] centroidLinkageClustering(Double[][] data, int numberOfClusters, ServerConfig serverConfig) throws InterruptedException, IOException {
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

            final CentroidDistance[] minDistance = {new CentroidDistance(Double.MAX_VALUE, -1, -1)};
            List<FutureCentroidDistance> futureCentroidDistances = new ArrayList<>();

            for (int i = 0; i < data.length; i++) {
                if (mapData.get(i) == null) continue;

                for (int j = i+1; j < data.length; j++) {
                    if (mapData.get(j) == null) continue;

                    futureCentroidDistances.add(new FutureCentroidDistance(centroids[i],centroids[j],i,j) {
                        @Override
                        public CentroidDistance call() throws Exception {
                            Double[] distMatrix = new Double[centroid1.length];
                            Double[] distMatrixT = new Double[centroid1.length];
                            for (int k = 0; k < distMatrix.length; k++) {
                                distMatrix[k] = centroid1[k];
                                distMatrixT[k] = centroid2[k];
                            }
                            double distance = mtm.getDistance(distMatrix, distMatrixT).get();
                            if (distance < minDistance[0].getDistance()){
                                minDistance[0] = new CentroidDistance(distance, left, right);
                            }
                            return new CentroidDistance(distance, left, right);
                        }
                    });

                }
            }

            executorService.invokeAll(futureCentroidDistances);


            left = minDistance[0].getLeftCentroid();
            right = minDistance[0].getRightCentroid();

            int finalLeft = left;
            int finalRight = right;
            if (minDistance[0].getLeftCentroid() < minDistance[0].getRightCentroid()){
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
