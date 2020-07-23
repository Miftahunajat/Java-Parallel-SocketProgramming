package com.miftahunajat.automatic;

import com.miftahunajat.model.ClusterAndVariance;
import com.miftahunajat.thread.MultiThreadManager;
import com.miftahunajat.tunnel.ServerConfig;
import com.miftahunajat.util.Core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class AutomaticClustering  {

    public int[] parallelHierarchicalAutomaticClustering(double[][] data, int _interval, ServerConfig serverConfig) throws IOException {
        int n = data.length;
        int interval = _interval;
        double[] V = new double[_interval + 1];
        int[][] clusterTable = new int[_interval + 2][data.length];
        double[] density = new double[_interval + 1];
        double[] variance;
        double[] output = new double[2];
        MultiThreadManager mtm = MultiThreadManager.getInstance(serverConfig);
        List<Future<ClusterAndVariance>> clusterers = new ArrayList<>();

        for (int i = n; i > 1; i--) {
            if (i <= _interval+1){
                clusterers.add(mtm.sendClusters(data, i));
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
            clusterTable[interval + 1] = Core.copyArray(clusterAndVariance.getClusters());
            interval--;
        }

        // Identify Global Optimum
        for(int i = _interval - 1; i > 0; --i) {
            if (V[i - 1] >= V[i] && V[i + 1] > V[i]) {
                density[i] = V[i + 1] + V[i - 1] - 2.0 * V[i];
            }
        }

        variance = Core.getMax(density);
        System.out.println(Arrays.toString(variance));

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
