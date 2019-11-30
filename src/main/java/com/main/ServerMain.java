package com.main;


import com.thread.MultiThreadManager;
import com.util.StringVector;
import com.util.VectorSpaceHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ServerMain {
    private static String vectorOperation = "[[1, 2, 3], [4, 5, 6], [7, 8, 9], [10, 11, 12]]||*||[[1, 2, 3], [4, 5, 6], [7, 8, 9]]";
    private static String expectedResult = "[1, 4, 9, 16, 25, 36, 49, 64]";
    private static List<ArrayList<Integer>> result = new ArrayList<>();
    private static int[][] matrix = new int[][]{{1,2,3},{4,5,6}, {7,8,9}};
    public static void main(String[] args) throws Exception {


        long start = System.currentTimeMillis();
        // maincode
        MultiThreadManager mtm = MultiThreadManager.getInstance();
        for (int i = 0; i < 60; i++) {
            double[][] mat1 = new com.bayudwiyansatria.mat.Mat().initArrayRandom(10,1000,1,1000.0);
            double[][] mat2 = new com.bayudwiyansatria.mat.Mat().initArrayRandom(1000,10_000,1,1000.0);
            double[][] mat3 = VectorSpaceHelper.multiplyTwoMatrices(mat1, mat2);

            System.out.println(i);
//            System.out.println(Arrays.deepToString(mat3));
//            try {
//                Thread.sleep(1500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            StringVector stringUtil = new StringVector(vectorOperation, true);
//            System.out.println(Arrays.deepToString(stringUtil.getVector1()));
//            mtm.startResult(Arrays.deepToString(new int[][]{{1,1, 1}, {1,1, 1}}) + "||*||" + Arrays.deepToString(new int[][]{{i, i+1, i+2}, {i, i+1, i+2}, {i, i+1, i+2}}));
//            System.out.println(mtm.getClientStatus());
//            System.out.println(Arrays.deepToString(results));

        }
//        mtm.close();
        //end of main

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
        System.out.println("Server Compute Count : " + mtm.serverComputeCount.get());
        System.out.println("Client Compute Count: " + mtm.clientComputeCount.toString());
        System.out.println("Temp Compute Count: " + mtm.temp.get());
    }
}
