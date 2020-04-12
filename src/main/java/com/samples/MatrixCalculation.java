package com.samples;

import com.thread.MultiThreadManager;
import com.util.Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MatrixCalculation {
    public static void main(String[] args) throws Exception {
        double[][] irisData = Core.readLargeCSV("src/main/resources/1kbigdata.csv");

        double[][] s100Data = Arrays.copyOfRange(irisData, 0,100);
        double[][] tS100Data = Core.transposeMatrix(s100Data);

        long start = System.currentTimeMillis();
        MultiThreadManager mtm = MultiThreadManager.getInstance();
        List<Future<Double[][]>> results = new ArrayList<>();
        // maincode
        for (int i = 0; i < 10; i++) {
        }
        System.out.println("finish");
        results.forEach(c-> {
            try {
                c.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });


        mtm.close();

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
    }

}
