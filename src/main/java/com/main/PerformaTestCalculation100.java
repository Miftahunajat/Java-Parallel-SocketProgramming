package com.main;

import com.thread.MultiThreadManager;
import com.util.Core;
import com.util.VectorSpaceHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

public class PerformaTestCalculation100 {
    public static void main(String[] args) throws Exception {
        double[][] irisData = Core.readLargeCSV("src/main/resources/1kbigdata.csv");

        double[][] s100Data = Arrays.copyOfRange(irisData, 0,100);
        double[][] tS100Data = new com.bayudwiyansatria.mat.Mat().transposeMatrix(s100Data);
//        System.out.println(Arrays.deepToString(irisData));

        long start = System.currentTimeMillis();
        MultiThreadManager mtm = MultiThreadManager.getInstance();
        LinkedList<Future<Double[][]>> hasilKaliMatrix = new LinkedList<>();
//        Future<Double[][]>[] hasilKaliMatrix = new Future[10_000];
        // maincode

        for (int i = 0; i < 100_000; i++) {
//            mtm.startResult(s100Data, tS100Data);
            mtm.startResult(s100Data, tS100Data).get();
//            hasilKaliMatrix.add();
//            VectorSpaceHelper.multiplyTwoMatrices(s100Data, tS100Data);
        }
        hasilKaliMatrix.stream().map(s -> {
            try {
                return s.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        });
        mtm.close();

//        MultiThreadManager mtm = MultiThreadManager.getInstance();

//        for (int i = 0; i < irisData.length; i++) {
//            for (int j = 0; j < irisData.length; j++) {
//                mtm.startResult(Arrays.toString(irisData[i]) + "||*||" + Arrays.toString(irisData[j]));
//            }
//        }
        //end of main

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
    }

}
