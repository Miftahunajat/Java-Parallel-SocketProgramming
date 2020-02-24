package com.main;

import com.Config;
import com.clustering.*;
import com.thread.MultiThreadManager;
import com.util.Core;
import com.util.VectorSpaceHelper;
import sun.security.ssl.HandshakeInStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

public class Testing {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        double[][] datas = Core.readLargeCSV("src/main/resources/1kbigdata.csv");
        try {
            int[] results = new BayuHierarchical().CentroidLinkage(datas, 3);
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
