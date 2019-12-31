package com.main;

import com.clustering.HierarchicalClustering;
import com.clustering.KMeansClustering;
import com.util.Core;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Testing {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        double[][] irises = Core.readLargeCSV("src/main/resources/2.5kbigdata.csv");
        try {
            int[] results = HierarchicalClustering.centroidLinkageClustering(irises, 3);
            System.out.println(Arrays.toString(results));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] cobas = {"a","b","c","d","f","g","h","i","j","k","l"};
        int[] selected = {0,1,2,3,4,5,6,7,8,9,5,11};
        int a = 5;

        String[] canvas = IntStream.range(0, cobas.length)
                .filter(i -> selected[i] == a)
                .mapToObj(i -> cobas[i])
                .toArray(String[]::new);
        System.out.println(Arrays.toString(canvas));



        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");

    }

}
