package com.main;

import com.thread.MultiThreadManager;
import com.util.Core;
import com.util.VectorSpaceHelper;
import org.apache.commons.lang3.ArrayUtils;

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
        Double[][] mat1O = Arrays.stream(new com.bayudwiyansatria.mat.Mat().initArrayRandom(10,1000,1,1000.0)).map(ArrayUtils::toObject).toArray(Double[][]::new);
        Double[][] mat2O = Arrays.stream(new com.bayudwiyansatria.mat.Mat().initArrayRandom(1000,10_000,1,1000.0)).map(ArrayUtils::toObject).toArray(Double[][]::new);

        MultiThreadManager mtm = MultiThreadManager.getInstance();
        LinkedList<Future<Double[][]>> hasilKaliMatrix = new LinkedList<>();
//        Future<Double[][]>[] hasilKaliMatrix = new Future[10_000];
        // maincode

        for (int i = 0; i < 20; i++) {
//            mtm.startResult(s100Data, tS100Data).get();
            hasilKaliMatrix.add(mtm.startResult(mat1O, mat2O));
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
        System.out.println(mtm.serverComputeCount);
        System.out.println(Arrays.toString(mtm.clientComputeCount));
    }

}
