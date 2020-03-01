package com.automatic;

import com.clustering.BayuHierarchical;
import com.clustering.ML;
import com.clustering.SerialHierarchicalClustering;
import com.model.ClusterAndVariance;
import com.thread.MultiThreadManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AutomaticClustering extends ML {

    public static int[] hierarchicalAutomaticClustering(Double[][] data){
        int n = data.length;
        for (int i = n; i > 1; i--) {
//            new BayuHierarchical().SingleLinkage(data,i)
        }
        return null;
    }

    public int[] hierarchicalAutomaticClustering(double[][] data, int _interval) {
        int n = data.length;
        int interval = _interval;
        double[] V = new double[_interval + 1];
        int[][] clusterTable = new int[_interval + 2][data.length];
        double[] density = new double[_interval + 1];
        double[] variance;
        double[] output = new double[2];
//        MultiThreadManager mtm = MultiThreadManager.getInstance();

        for (int i = n; i > 1; i--) {
            if (i <= _interval+1){
                int[] clustersResult = new BayuHierarchical().CentroidLinkage(data,i);
                variance = getVariance(data, clustersResult);
                V[interval] = variance[0] / variance[1];
                clusterTable[interval + 1] = new com.bayudwiyansatria.mat.Mat().copyArray(clustersResult);
                interval--;
            }
        }

        for(int j = _interval - 1; j > 0; --j) {
            if (V[j - 1] >= V[j] && V[j + 1] > V[j]) {
                density[j] = V[j + 1] + V[j - 1] - 2.0 * V[j];
            }
        }

        variance = new com.bayudwiyansatria.math.Math().getMax(density);
        int optimum = (int) variance[1] + 1;
        density[optimum - 1] = -1.0;
        double globalOptimum;
        if (variance[0] == 0.0) {
            globalOptimum = 0.0;
        } else {
            globalOptimum = variance[0] / variance[0];
        }

        output[0] = optimum;
        output[1] = globalOptimum;
        return clusterTable[optimum];
    }

    public int[] parallelHierarchicalAutomaticClustering(double[][] data, int _interval) throws IOException {
        int n = data.length;
        int interval = _interval;
        double[] V = new double[_interval + 1];
        int[][] clusterTable = new int[_interval + 2][data.length];
        double[] density = new double[_interval + 1];
        double[] variance;
        double[] output = new double[2];
        MultiThreadManager mtm = MultiThreadManager.getInstance();
        List<Future<ClusterAndVariance>> clusterers = new ArrayList<>();

        for (int i = n; i > 1; i--) {
            if (i <= _interval+1){
                clusterers.add(mtm.sendClusters(data, i));
//                int[] clustersResult = SerialHierarchicalClustering.pCentroidLinkageClustering(data,i);
//                int[] clustersResult = new BayuHierarchical().CentroidLinkage(data, i);
//                variance = getVariance(data, clustersResult);
//                V[interval] = variance[0] / variance[1];
//                clusterTable[interval + 1] = new com.bayudwiyansatria.mat.Mat().copyArray(clustersResult);
//                interval--;
            }
        }
        List<ClusterAndVariance> clusterAndVariances = clusterers.stream().map(clusterAndVarianceFuture -> {
            try {
                return clusterAndVarianceFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        for (ClusterAndVariance clusterAndVariance : clusterAndVariances) {
            variance = clusterAndVariance.getVariances();
            V[interval] = variance[0] / variance[1];
            clusterTable[interval + 1] = new com.bayudwiyansatria.mat.Mat().copyArray(clusterAndVariance.getClusters());
            interval--;
        }

        for(int j = _interval - 1; j > 0; --j) {
            if (V[j - 1] >= V[j] && V[j + 1] > V[j]) {
                density[j] = V[j + 1] + V[j - 1] - 2.0 * V[j];
            }
        }

        variance = new com.bayudwiyansatria.math.Math().getMax(density);
        int optimum = (int) variance[1] + 1;
        density[optimum - 1] = -1.0;
        double globalOptimum;
        if (variance[0] == 0.0) {
            globalOptimum = 0.0;
        } else {
            globalOptimum = variance[0] / variance[0];
        }

        output[0] = optimum;
        output[1] = globalOptimum;
        return clusterTable[optimum];
    }
}
