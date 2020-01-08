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
        double[][] datas = Core.readLargeCSV(Config.fileLocation);
        try {
            int[] results = PGHierarchicalClustering.centroidLinkageClustering(datas, 3);
            System.out.println(Arrays.toString(results));
        } catch (Exception e) {
            e.printStackTrace();
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");

        double[][] arrays = new double[][]{{0,1,2,3,4,5}};
        double[][] arrays2 = new double[][]{{0,1,2,3,4,5}};
//        int[] dats = Arrays.copyOfRange(arrays, 0,3);
        double result[][] = VectorSpaceHelper.substractTwoMatrices(arrays, arrays2);
        System.out.println(Arrays.deepToString(result));

    }

}
