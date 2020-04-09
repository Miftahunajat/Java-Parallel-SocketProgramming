package com.samples;

import com.Config;
import com.automatic.AutomaticClustering;
import com.util.Core;

import java.util.Arrays;

public class AutomaticClusteringMain {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        double[][] datas = Core.readLargeCSV(Config.fileLocation);
        try {
            int[] results = new AutomaticClustering().parallelHierarchicalAutomaticClustering(datas, Config.INTERVAL);
//            int[] results = SerialHierarchicalClustering.centroidLinkageClustering(datas, 10);
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
