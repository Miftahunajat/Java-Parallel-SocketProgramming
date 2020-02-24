package com.clustering;

import com.Config;
import com.bayudwiyansatria.mat.Mat;
import com.bayudwiyansatria.mat.Vector;
import com.thread.MultiThreadManager;
import com.util.Core;
import com.util.VectorSpaceHelper;
import org.omg.SendingContext.RunTime;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PG2HierarchicalClustering {

    public static int[] centroidLinkageClustering(Double[][] data, int numberOfClusters) throws Exception {
        MultiThreadManager mtm = MultiThreadManager.getInstance();
        int core = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(core);
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
        while (currentClusterCount != numberOfClusters) {
            List<Future<Double[][]>> futureCentroidDistances = new ArrayList<>();
            List<Double> oldRangeI = new ArrayList<>();
            List<Double[]> oldDataCentroidsI = new ArrayList<>();
            List<Double> oldRangeJ = new ArrayList<>();
            List<Double[]> oldDataCentroidsJ = new ArrayList<>();

            for (int i = 0; i < data.length; i++) {
                if (mapData.get(i) == null) {
                    continue;
                }

                for (int j = i + 1; j < data.length; j++) {
                    if (mapData.get(j) == null) continue;
                    oldRangeI.add((double) i);
                    oldRangeJ.add((double) j);
                    oldDataCentroidsI.add(centroids[i].clone());
                    oldDataCentroidsJ.add(centroids[j].clone());
                }
             }

            int startIndex = 0;
            int len = oldDataCentroidsI.size();

            Double[] rangeI = new Double[0];
            Double[][] dataCentroidsI;
            Double[] rangeJ = new Double[0];;
            Double[][] dataCentroidsJ;
            int divider = Config.DIVIDER;
//            System.out.println(len / divider);
//            System.out.println(len/divider);
            if (len / divider > Config.MAX_MATRIX_COUNT) {
                for (int i = 1; i <= divider; i++) {
                    rangeI = oldRangeI.subList(startIndex, len * i / divider).toArray(rangeI);
                    dataCentroidsI = Core.convertListArrayToDouble(oldDataCentroidsI.subList(startIndex, len * i / divider));
                    rangeJ = oldRangeJ.subList(startIndex, len * i / divider).toArray(rangeJ);
                    dataCentroidsJ = Core.convertListArrayToDouble(oldDataCentroidsJ.subList(startIndex, len * i / divider));
                    startIndex = len * i / divider;
                    futureCentroidDistances.add(mtm.getDistanceMetric(dataCentroidsI, dataCentroidsJ, rangeI, rangeJ));
                }
            } else {
                rangeI = oldRangeI.toArray(rangeI);
                dataCentroidsI = Core.convertListArrayToDouble(oldDataCentroidsI);
                rangeJ = oldRangeJ.toArray(rangeJ);
                dataCentroidsJ = Core.convertListArrayToDouble(oldDataCentroidsJ);
                futureCentroidDistances.add(mtm.getDistanceMetricMain(dataCentroidsI, dataCentroidsJ, rangeI, rangeJ));
            }

            double[] distanceMin = new double[]{Double.MAX_VALUE, -1, -1};
            futureCentroidDistances.stream().forEach(result -> {
                try {
                    Double[][] res = result.get();
                    Arrays.stream(res).forEach(k -> {
                        if (k[0] < distanceMin[0]) {
                            distanceMin[0] = k[0];
                            distanceMin[1] = k[1];
                            distanceMin[2] = k[2];
                        }
                    });

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });

            left = (int) distanceMin[1];
            right = (int) distanceMin[2];

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
        executorService.shutdownNow();
        mtm.close();
        System.out.println("Server Count : " + mtm.serverComputeCount);
        System.out.println("Client Count : " + Arrays.toString(mtm.clientComputeCount));
        return selCentroids;
    }
}
