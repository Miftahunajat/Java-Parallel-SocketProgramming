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
        for (int i = 0; i < 60_000; i++) {

            System.out.println(i);
            mtm.startResult(vectorOperation);
            System.out.println(mtm.getClientStatus());

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
