package com.clustering;

import com.thread.MultiThreadManager;
import com.util.Core;

import java.util.*;
import java.util.concurrent.Future;

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
            System.out.println(currentClusterCount);


//            List<CentroidDistance> synchronizedList = Collections.synchronizedList(distances);
//
//            System.out.println(currentClusterCount);
//            mapData.entrySet().stream().forEach(map->{
//                for (int j = map.getKey(); j < data.length; j++) {
//                    if (mapData.get(j) == null) continue;
////
//                    double[][] data2 = mapData.get(j);
//
//
//                    double distance = centroidLinkage(map.getValue(), data2);
////                    double distance = 0;
//                    synchronizedList.add(new CentroidDistance(distance,map.getKey(),j));
//                }
//            });


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

//    public static int[] parallelHierarchicalClustering(double[][] datum, int numberOfClusters) throws IOException {
//        MultiThreadManager mtm = MultiThreadManager.getInstance();
//        Double[][] newData = Arrays.stream(datum).map(ArrayUtils::toObject).toArray(Double[][]::new);
//        int currentClusterCount = newData.length;
//        int[] selCentroids = new int[newData.length];
//        for (int i = 0; i < selCentroids.length; i++) {
//            selCentroids[i] = i;
//        }
//        while (currentClusterCount != numberOfClusters){
//
//            double distanceMin = Double.MAX_VALUE;
//            int left = -1;
//            int right = -1;
//
//            List<Future<CentroidDistance>> distances = new ArrayList<>();
//
//            for (int i = 0; i < newData.length; i++) {
//                for (int j = i+1; j < newData.length; j++) {
//                    int finalI = i;
//                    int[] finalSelCentroids = selCentroids;
//                    Double[][] data1 = IntStream.range(0, newData.length)
//                            .filter(index -> finalSelCentroids[index] == finalI)
//                            .mapToObj(index -> newData[index])
//                            .toArray(Double[][]::new);
//
//                    int finalJ = j;
//                    Double[][] data2 = IntStream.range(0, newData.length)
//                            .filter(index -> finalSelCentroids[index] == finalJ)
//                            .mapToObj(index -> newData[index])
//                            .toArray(Double[][]::new);
//
//                    if (data1.length == 0 || data2.length == 0) continue;
//
//                    Future<Double> distance = centroidLinkage(data1, data2, mtm);
//                    distances.add(new CentroidDistance(distance,i,j));
//                }
//            }
//
//            CentroidDistance minDistance = distances.parallelStream().min(Comparator.comparing(CentroidDistance::getDistance)).get();
//            left = minDistance.getLeftCentroid();
//            right = minDistance.getRightCentroid();
//            if (minDistance.getLeftCentroid() < minDistance.getRightCentroid()){
//                int finalRight = right;
//                int finalLeft = left;
//                selCentroids = Arrays
//                        .stream(selCentroids).map(idx -> idx == finalRight ? finalLeft : idx)
//                        .toArray();
//            }else{
//                int finalRight = right;
//                int finalLeft = left;
//                selCentroids = Arrays
//                        .stream(selCentroids).map(idx -> idx == finalLeft ? finalRight : idx)
//                        .toArray();
//            }
//            currentClusterCount--;
//
//        }
//        mtm.close();
//        return selCentroids;
//    }

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
}
