package com.miftahunajat.samples;

import com.miftahunajat.automatic.Statistic;
import com.miftahunajat.clustering.HierarchicalClustering;
import com.miftahunajat.util.Core;

import java.util.Arrays;

public class ClusteringExample {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        double[][] data = Core.readLargeCSV("src/main/resources/tripleruspini.csv");
        int NumberOfCluster = 4;
        System.out.println("lewat");
        int[] results = HierarchicalClustering.centroidLinkageClustering(data, NumberOfCluster);
        System.out.println(Arrays.toString(results));

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
    }
}
