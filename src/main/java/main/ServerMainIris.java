package main;

import com.bayudwiyansatria.io.IO;
import main.thread.MultiThreadManager;
import main.util.StringVector;
import main.util.VectorSpaceHelper;

import java.io.IOException;
import java.util.Arrays;

public class ServerMainIris {
    public static void main(String[] args) throws IOException {
        double[][] irisData = new IO().readCSV_double("iris");

        long start = System.currentTimeMillis();
        // maincode
        MultiThreadManager mtm = MultiThreadManager.getInstance();
        for (int i = 0; i < 60; i++) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            StringVector stringUtil = new StringVector(vectorOperation);
//            System.out.println(Arrays.deepToString(stringUtil.getVector1()));
//            int[][] results = VectorSpaceHelper.multiplyTwoMatrices(
//                    new int[][]{ {1,1}, {1,1} },new int[][]{{i, i+1, i+2}, {i, i+1, i+2}}
//            );
//            System.out.println(Arrays.deepToString(results));

        }
        //end of main

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
    }
}
