package main;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import foo.thread.MultiThreadManager;
import foo.util.StringVector;
import foo.util.VectorSpaceHelper;

public class ServerMain {
    private static String vectorOperation = "[[1, 2, 3], [4, 5, 6], [7, 8, 9], [10, 11, 12]]||*||[[1, 2, 3], [4, 5, 6], [7, 8, 9]]";
    private static String expectedResult = "[1, 4, 9, 16, 25, 36, 49, 64]";
    private static List<ArrayList<Integer>> result = new ArrayList<>();
    private static int[][] matrix = new int[][]{{1,2,3},{4,5,6}, {7,8,9}};
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        // maincode
        MultiThreadManager mtm = MultiThreadManager.getInstance();
        for (int i = 0; i < 60; i++) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            StringVector stringUtil = new StringVector(vectorOperation);
//            System.out.println(Arrays.deepToString(stringUtil.getVector1()));
            int[][] results = VectorSpaceHelper.multiplyTwoMatrices(new int[]{i,i+1,i+2,i+3},new int[]{0,0,0,0});
//            String stringResult = Arrays.toString(results);
//            mtm.startResult(stringResult);
            System.out.println(results);

        }
        //end of main

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
    }
}
