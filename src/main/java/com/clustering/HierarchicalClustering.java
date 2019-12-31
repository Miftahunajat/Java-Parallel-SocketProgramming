package com.clustering;

import com.bayudwiyansatria.mat.Mat;
import com.bayudwiyansatria.mat.Vector;
import com.thread.MultiThreadManager;
import com.util.Core;
import org.apache.commons.lang3.ArrayUtils;
import org.jcp.xml.dsig.internal.dom.Utils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

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

            double distanceMin = Double.MAX_VALUE;
            int left = -1;
            int right = -1;

            List<CentroidDistance> distances = new ArrayList<>();
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


            for (int i = 0; i < data.length; i++) {
                if (mapData.get(i) == null) continue;
                double[][] data1 = mapData.get(i);

                for (int j = i+1; j < data.length; j++) {
                    if (mapData.get(j) == null) continue;
//
                    double[][] data2 = mapData.get(j);


                    double distance = centroidLinkage(centroids[i], centroids[j]);
//                    double distance = 0;
                    distances.add(new CentroidDistance(distance,i,j));
                }
            }


            CentroidDistance minDistance = distances.parallelStream().min(Comparator.comparing(CentroidDistance::getDistance)).get();
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
        double[][] distanceMetric = new com.bayudwiyansatria.mat.Vector().getDistanceMetric(data);
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
        double[][] distanceMetric = new com.bayudwiyansatria.mat.Mat().getDistanceMetric(data);
        int[] clusters = new int[data.length];
        double[][] newData = new com.bayudwiyansatria.mat.Mat().copyArray(data);
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
                    double DistanceBetweenCentroid = new com.bayudwiyansatria.mat.Mat().getDistance(newData[x], newData[y]);

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
        double[][] distanceMetric = new com.bayudwiyansatria.mat.Mat().getDistanceMetric(data);
        double[][] distanceTable = new com.bayudwiyansatria.mat.Mat().copyArray(distanceMetric);
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
        double[][] distanceMetric = new com.bayudwiyansatria.mat.Mat().getDistanceMetric(data);
        double[][] distanceTable = new com.bayudwiyansatria.mat.Mat().copyArray(distanceMetric);
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
        int[] label = new com.bayudwiyansatria.mat.Mat().copyArray(clusters);
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
