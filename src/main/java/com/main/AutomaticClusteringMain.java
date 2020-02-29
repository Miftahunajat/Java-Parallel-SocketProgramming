package com.main;

import com.automatic.AutomaticClustering;
import com.clustering.BayuHierarchical;
import com.clustering.ParallelHierarchicalClustering;
import com.util.Core;

import java.util.Arrays;

public class AutomaticClusteringMain {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        double[][] datas = Core.readLargeCSV("src/main/resources/1kbigdata.csv");
        try {
//            int[] results = new AutomaticClustering().hierarchicalAutomaticClustering(datas, 50);
            int[] results = new BayuHierarchical().CentroidLinkage(datas, 10);
            System.out.println(Arrays.toString(results));
        } catch (Exception e) {
            e.printStackTrace();
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");



    }
}
