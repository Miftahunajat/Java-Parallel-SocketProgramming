package com.samples;

import com.clustering.*;
import com.util.Core;

import java.util.Arrays;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        Double[][] datas = Core.readLargeCSVWrapper("src/main/resources/1kbigdata.csv");
        try {
            int[] results = ParallelHierarchicalClustering.centroidLinkageClustering(datas, 3);
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
