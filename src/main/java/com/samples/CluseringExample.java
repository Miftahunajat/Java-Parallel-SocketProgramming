package com.samples;

import com.clustering.HierarchicalClustering;
import com.util.Core;

import java.util.Arrays;

public class CluseringExample {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        double[][] data = Core.readLargeCSV("src/main/resources/2.5kbigdata");
        int NumberOfCluster = 4;
        System.out.println("lewat");
        int[] results = new HierarchicalClustering().CentroidLinkage(data, NumberOfCluster);
        System.out.println(Arrays.toString(results));

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
    }
}
