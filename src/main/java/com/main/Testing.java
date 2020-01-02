package com.main;

import com.clustering.HierarchicalClustering;
import com.clustering.KMeansClustering;
import com.clustering.ParallelHierarchicalClustering;
import com.util.Core;
import sun.security.ssl.HandshakeInStream;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Testing {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        double[][] datas = Core.readLargeCSV("src/main/resources/ruspini.csv");
        try {
            int[] results = ParallelHierarchicalClustering.centroidLinkageClustering(datas, 3);
            System.out.println(Arrays.toString(results));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] cobas = {"a","b","c","d","f","g","h","i","j","k","l"};
        int[] selected = {0,1,2,3,4,5,6,7,8,9,5,11};

        String[] canvas = IntStream.range(0, cobas.length)
                .filter(i -> selected[i] == 5)
                .mapToObj(i -> cobas[i])
                .toArray(String[]::new);
        System.out.println(Arrays.toString(canvas));



        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");

    }

}
