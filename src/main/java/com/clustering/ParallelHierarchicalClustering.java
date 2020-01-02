package com.clustering;

import com.bayudwiyansatria.mat.Mat;
import com.bayudwiyansatria.mat.Vector;
import com.thread.MultiThreadManager;
import com.util.Core;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class ParallelHierarchicalClustering {

//    static MultiThreadManager mtm;

    public static int[] centroidLinkageClustering(double[][] data, int numberOfClusters) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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

            final CentroidDistance[] minDistance = {new CentroidDistance(Double.MAX_VALUE, -1, -1)};
            List<FutureCentroidDistance> futureCentroidDistances = new ArrayList<>();

            for (int i = 0; i < data.length; i++) {
                if (mapData.get(i) == null) continue;

                for (int j = i+1; j < data.length; j++) {
                    if (mapData.get(j) == null) continue;

                    futureCentroidDistances.add(new FutureCentroidDistance(centroids[i],centroids[j],i,j) {
                        @Override
                        public CentroidDistance call() throws Exception {
                            double distance = centroidLinkage(centroids[i], centroids[j]);
                            if (distance < minDistance[0].getDistance()){
                                minDistance[0] = new CentroidDistance(distance,i,j);
                            }
                            return new CentroidDistance(distance, i,j);
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
        executorService.shutdownNow();
        return selCentroids;
    }
//    public static int[] dump1CentroidLinkageClustering(double[][] data, int numberOfClusters) throws IOException {
//        int currentClusterCount = data.length;
//        int[] selCentroids = new int[data.length];
//        double[][] centroids = new double[data.length][];
//        ExecutorService executorService;
////        mtm = MultiThreadManager.getInstance();
//
//        Map<Integer, double[][]> mapData = new HashMap<>();
//
//        for (int i = 0; i < data.length; i++) {
//            mapData.put(i, new double[][]{data[i]});
//            selCentroids[i] = i;
//            centroids[i] = data[i].clone();
//        }
//        List<Future<CentroidDistance>> distances = new ArrayList<>();
//        while (currentClusterCount != numberOfClusters){
//            executorService = Executors.newFixedThreadPool(3);
//
//            int left = -1;
//            int right = -1;
//
//            final CentroidDistance[] minDistance = {new CentroidDistance(Double.MAX_VALUE, -1, -1)};
//            for (int i = 0; i < data.length; i++) {
//                if (mapData.get(i) == null) continue;
//
//                for (int j = i+1; j < data.length; j++) {
//                    if (mapData.get(j) == null) continue;
//                    executorService.execute(new FutureCentroidDistance(centroids[i], centroids[j],i,j) {
//                        @Override
//                        public void run() {
//                            double distance = 0;
//                            for (int i = 0; i < centroid1.length; i++) {
//                                distance += Math.pow((centroid1[i] - centroid2[i]),2);
//                            }
////                            distance = new Math.sqrt()
////                            return Math.sqrt(retval);
//                            if (distance < minDistance[0].getDistance()){
//                                minDistance[0] = new CentroidDistance(distance, i, j);
//
//                            }
//                        }
//                    });
//                }
//            }
//            executorService.shutdown();
//
////            left = minDistance[0].getLeftCentroid();
////            right = minDistance[0].getRightCentroid();
////
////            int finalLeft = left;
////            int finalRight = right;
//////            System.out.println(currentClusterCount);
////
////            if (left < right){
////                double[][] newData = Core.joinMultipleArray(mapData.get(left), mapData.get(right));
////                mapData.remove(right);
////                mapData.put(left, newData);
////                selCentroids = Arrays
////                        .stream(selCentroids).map(idx -> idx == finalRight ? finalLeft : idx)
////                        .toArray();
////                centroids[left] = Core.getCentroidsFromDouble(newData);
////            }else{
////                double[][] newData = Core.joinMultipleArray(mapData.get(right), mapData.get(left));
////                mapData.remove(left);
////                mapData.put(right, newData);
////                selCentroids = Arrays
////                        .stream(selCentroids).map(idx -> idx == finalLeft ? finalRight : idx)
////                        .toArray();
////                centroids[right] = Core.getCentroidsFromDouble(newData);
////            }
//
//            currentClusterCount--;
//
//        }
////        executorService.shutdown();
//        return selCentroids;
//    }

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
                    double distance = new Vector().getDistance(centroids[i], centroids[j] );
                }
            }
             currentClusters--;
        }while (currentClusters < numberOfCluster);
//        new Vector().getDistance()
        return new int[]{0};
    }

    public int[] SingleLinkage(double[][] data, int NumberOfCluster) {
        double[][] distanceMetric = new Vector().getDistanceMetric(data);
        int[] clusters = new int[data.length];
        int length = data.length;

        for(int i = 0; i < data.length; i++) {
            clusters[i] = i;
        }

        int counter = 0;
        do {
            System.out.println(length);
            int x = 1;
            int y = 0;
            double distance = distanceMetric[x - 1][y];

            for(int i = 0; i < (data.length-1); ++i) {
                for(int j = 0; j <= i; ++j) {
                    if (distance < 0.0 || distanceMetric[i][j] < distance && distanceMetric[i][j] > -1.0) {
                        distance = distanceMetric[i][j];
                        x = i + 1;
                        y = j;
                    }
                }
            }

            int left;
            int right;
            if (clusters[x] < clusters[y]) {
                left = clusters[y];
                right = clusters[x];
            } else {
                left = clusters[x];
                right = clusters[y];
            }

            int[] position = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", left);

            for(int i = 0; i < position.length; ++i) {
                clusters[position[i]] = right;
            }

            position = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", right);
            --length;

            for(int i = 0; i < position.length; ++i) {
                for(int j = 0; j < position.length; ++j) {
                    if (i != j) {
                        if (position[i] > position[j]) {
                            distanceMetric[position[i] - 1][position[j]] = -1.0;
                        } else {
                            distanceMetric[position[j] - 1][position[i]] = -1.0;
                        }
                    }
                }
            }

            for(int i = 0; i < data.length - 1; ++i) {
                double member = -1.0;
                double value = -1.0;

                for(int j = 0; j < position.length; ++j) {
                    if (i + 1 > position[j]) {
                        value = distanceMetric[i][position[j]];
                    } else if (i + 1 < position[j]) {
                        value = distanceMetric[position[j] - 1][i + 1];
                    }

                    if (member < 0.0 || value < member) {
                        member = value;
                    }
                }

                if (member > -1.0) {
                    for(int j = 0; j < position.length; ++j) {
                        if (i + 1 > position[j]) {
                            distanceMetric[i][position[j]] = member;
                        } else if (i + 1 < position[j]) {
                            distanceMetric[position[j] - 1][i + 1] = member;
                        }
                    }
                }
            }
        } while(length > NumberOfCluster);

        clusters = this.getNormalLabel(clusters);

        return clusters;
    }

    public int[] SingleLinkage(int[][] data, int NumberOfCluster) {
        int[] clusters = this.SingleLinkage(new com.bayudwiyansatria.utils.Utils().int_to_double(data), NumberOfCluster);
        return clusters;
    }

    public int[] SingleLinkage(String[][] data, int NumberOfCluster){
        int cluster[] = null;
        return cluster;
    }

    public int[] CentroidLinkage(double[][] data, int NumberOfCluster) {
        double[][] distanceMetric = new Mat().getDistanceMetric(data);
        int[] clusters = new int[data.length];
        double[][] newData = new Mat().copyArray(data);
        int length = data.length;

        for(int i = 0; i < data.length; i++) {
            clusters[i] = i;
        }

        while(length > NumberOfCluster) {
            int x = 1;
            int y = 0;
            double distance = distanceMetric[x - 1][y];

            for(int i = 0; i < data.length - 1; ++i) {
                for(int j = 0; j <= i; ++j) {
                    if (distance < 0.0 || distanceMetric[i][j] < distance && distanceMetric[i][j] > -1.0) {
                        distance = distanceMetric[i][j];
                        x = i + 1;
                        y = j;
                    }
                }
            }

            int left;
            int right;
            if (clusters[x] < clusters[y]) {
                left = clusters[y];
                right = clusters[x];
            } else {
                left = clusters[x];
                right = clusters[y];
            }

            int[] position = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", left);

            for(int i = 0; i < position.length; ++i) {
                clusters[position[i]] = right;
            }

            position = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", right);
            --length;

            for(int i = 0; i < position.length - 1; ++i) {
                for(int j = i + 1; j < position.length; ++j) {
                    if (position[i] > position[j]) {
                        distanceMetric[position[i] - 1][position[j]] = -1.0;
                    } else {
                        distanceMetric[position[j] - 1][position[i]] = -1.0;
                    }
                }
            }

            clusters = this.getNormalLabel(clusters);

            int[][] member = new int[length][];
            int[] nMember = new int[length];
            double[][] centroid = new double[length][data[0].length];
            System.arraycopy(newData, 0, centroid, 0, right);

            for(int i = 0; i < length; ++i) {
                member[i] = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", i);
                nMember[i] = member[i].length;
            }

            double[][] tmp = new double[nMember[right]][];

            for(int i = 0; i < nMember[right]; ++i) {
                tmp[i] = data[member[right][i]];
            }

            centroid[right] = this.getCentroid(tmp);
            System.arraycopy(newData, right + 1, centroid, right + 1, left - right - 1);
            System.arraycopy(newData, left + 1, centroid, left, length - left);
            newData = centroid;
            x = right;

            for(y = 0; y < length; ++y) {
                if (x != y) {
                    double DistanceBetweenCentroid = new Mat().getDistance(newData[x], newData[y]);

                    for(int i = 0; i < nMember[x]; ++i) {
                        for(int j = 0; j < nMember[y]; ++j) {
                            if (member[x][i] > member[y][j]) {
                                distanceMetric[member[x][i] - 1][member[y][j]] = DistanceBetweenCentroid;
                            } else {
                                distanceMetric[member[y][j] - 1][member[x][i]] = DistanceBetweenCentroid;
                            }
                        }
                    }
                }
            }
        }

        return clusters;
    }

    public int[] CentroidLinkage(int[][] data, int NumberOfCluster) {
        int[] clusters = this.CentroidLinkage(new com.bayudwiyansatria.utils.Utils().int_to_double(data), NumberOfCluster);
        return clusters;
    }

    public int[] CentroidLinkage(String[][] data, int NumberOfCluster){
        int cluster[] = null;
        return cluster;
    }

    public int[] CompleteLinkage(double[][] data, int NumberOfCluster) {
        double[][] distanceMetric = new Mat().getDistanceMetric(data);
        double[][] distanceTable = new Mat().copyArray(distanceMetric);
        int[] clusters = new int[data.length];
        int length = data.length;

        for(int i = 0; i < data.length; i++) {
            clusters[i] = i;
        }

        do {
            int x = 1;
            int y = 0;
            double distance = distanceMetric[x - 1][y];

            for(int i = 0; i < data.length - 1; ++i) {
                for(int j = 0; j <= i; ++j) {
                    if (distance < 0.0 || distanceMetric[i][j] < distance && distanceMetric[i][j] > -1.0) {
                        distance = distanceMetric[i][j];
                        x = i + 1;
                        y = j;
                    }
                }
            }

            int left;
            int right;
            if (clusters[x] < clusters[y]) {
                left = clusters[y];
                right = clusters[x];
            } else {
                left = clusters[x];
                right = clusters[y];
            }

            int[] cluster = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", left);

            for (int item : cluster) {
                clusters[item] = right;
            }

            cluster = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", right);
            --length;

            for(int i = 0; i < cluster.length - 1; ++i) {
                for(int j = i + 1; j < cluster.length; ++j) {
                    if (cluster[i] > cluster[j]) {
                        distanceMetric[cluster[i] - 1][cluster[j]] = -1.0;
                    } else {
                        distanceMetric[cluster[j] - 1][cluster[i]] = -1.0;
                    }
                }
            }

            clusters = this.getNormalLabel(clusters);
            int[][] member = new int[length][];
            int[] nMember = new int[length];

            for(int i = 0; i < length; ++i) {
                member[i] = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", i);
                nMember[i] = member[i].length;
            }

            x = right;

            for(y = 0; y < length; ++y) {
                if (x != y) {
                    double maxValue = -1.0;

                    for(int i = 0; i < nMember[x]; ++i) {
                        for(int j = 0; j < nMember[y]; ++j) {
                            double value;
                            if (member[x][i] > member[y][j]) {
                                value = distanceTable[member[x][i] - 1][member[y][j]];
                            } else {
                                value = distanceTable[member[y][j] - 1][member[x][i]];
                            }

                            if (maxValue < 0.0 || value > maxValue) {
                                maxValue = value;
                            }
                        }
                    }

                    for(int i = 0; i < nMember[x]; ++i) {
                        for(int j = 0; j < nMember[y]; ++j) {
                            if (member[x][i] > member[y][j]) {
                                distanceMetric[member[x][i] - 1][member[y][j]] = maxValue;
                            } else {
                                distanceMetric[member[y][j] - 1][member[x][i]] = maxValue;
                            }
                        }
                    }
                }
            }
        } while(length > NumberOfCluster);
        return clusters;
    }

    public int[] CompleteLinkage(int[][] data, int NumberOfCluster) {
        int[] clusters = this.CompleteLinkage(new com.bayudwiyansatria.utils.Utils().int_to_double(data), NumberOfCluster);
        return clusters;
    }

    public int[] CompleteLinkage(String[][] data, int NumberOfCluster){
        int cluster[] = null;
        return cluster;
    }

    public int[] AverageLinkage(double[][] data, int NumberOfCluster){
        double[][] distanceMetric = new Mat().getDistanceMetric(data);
        double[][] distanceTable = new Mat().copyArray(distanceMetric);
        int[] clusters = new int[data.length];
        int length = data.length;

        for(int i = 0; i < data.length; i++) {
            clusters[i] = i;
        }

        do {
            int x = 1;
            int y = 0;
            double distance = distanceMetric[x - 1][y];

            for(int i = 0; i < data.length - 1; ++i) {
                for(int j = 0; j <= i; ++j) {
                    if (distance < 0.0 || distanceMetric[i][j] < distance && distanceMetric[i][j] > -1.0) {
                        distance = distanceMetric[i][j];
                        x = i + 1;
                        y = j;
                    }
                }
            }

            int left;
            int right;
            if (clusters[x] < clusters[y]) {
                left = clusters[y];
                right = clusters[x];
            } else {
                left = clusters[x];
                right = clusters[y];
            }

            int[] cluster = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", left);

            for(int i = 0; i < cluster.length; ++i) {
                clusters[cluster[i]] = right;
            }

            cluster = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", right);
            --length;

            for(int i = 0; i < cluster.length - 1; ++i) {
                for(int j = i + 1; j < cluster.length; ++j) {
                    if (cluster[i] > cluster[j]) {
                        distanceMetric[cluster[i] - 1][cluster[j]] = -1.0;
                    } else {
                        distanceMetric[cluster[j] - 1][cluster[i]] = -1.0;
                    }
                }
            }

            clusters = this.getNormalLabel(clusters);
            int[][] member = new int[length][];
            int[] nMember = new int[length];

            for(int i = 0; i < length; ++i) {
                member[i] = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", i);
                nMember[i] = member[i].length;
            }

            x = right;

            for(y = 0; y < length; ++y) {
                if (x != y) {
                    double mean = 0.0;

                    for(int i = 0; i < nMember[x]; ++i) {
                        for(int j = 0; j < nMember[y]; ++j) {
                            double value;
                            if (member[x][i] > member[y][j]) {
                                value = distanceTable[member[x][i] - 1][member[y][j]];
                            } else {
                                value = distanceTable[member[y][j] - 1][member[x][i]];
                            }

                            mean = mean + value;
                        }
                    }

                    mean = mean / (nMember[x] * nMember[y]);

                    for(int i = 0; i < nMember[x]; ++i) {
                        for(int j = 0; j < nMember[y]; ++j) {
                            if (member[x][i] > member[y][j]) {
                                distanceMetric[member[x][i] - 1][member[y][j]] = mean;
                            } else {
                                distanceMetric[member[y][j] - 1][member[x][i]] = mean;
                            }
                        }
                    }
                }
            }
        } while(length > NumberOfCluster);

        return clusters;
    }

    public int[] AverageLinkage(int[][] data, int NumberOfCluster){
        int[] clusters = this.AverageLinkage(new com.bayudwiyansatria.utils.Utils().int_to_double(data), NumberOfCluster);
        return clusters;
    }

    public int[] AverageLinkage(String[][] data, int NumberOfCluster){
        int cluster[] = null;
        return cluster;
    }

    public int[] getNormalLabel(int[] clusters) {
        int[] label = new Mat().copyArray(clusters);
        int x = 0;
        int y = 0;
        int clusterCenter = -1;
        for(int i = 0; i < clusters.length; ++i) {
            if (label[i] > clusterCenter) {
                clusterCenter = label[i];
                ++y;
            }
        }

        do {
            int[] clusterLabel = new com.bayudwiyansatria.utils.Utils().getFind(label, "=", x);
            if (clusterLabel.length == 0) {
                int i;
                for(i = 0; i < clusters.length && label[i] < x; ++i) {
                }

                if (i < clusters.length) {
                    int[] cluster = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", clusters[i]);

                    for(int j = 0; j < cluster.length; ++j) {
                        label[cluster[j]] = x;
                    }
                }
            }
            ++x;
        } while(x < y);

        return label;
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
