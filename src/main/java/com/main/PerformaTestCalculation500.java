package com.main;

import com.thread.MultiThreadManager;
import com.util.Core;
import com.util.VectorSpaceHelper;

import java.util.Arrays;

public class PerformaTestCalculation500 {
    public static void main(String[] args) throws Exception {
        double[][] irisData = Core.readLargeCSV("src/main/resources/1kbigdata.csv");

        double[][] s100Data = Arrays.copyOfRange(irisData, 0,500);
        double[][] tS100Data = new com.bayudwiyansatria.mat.Mat().transposeMatrix(s100Data);
//        System.out.println(Arrays.deepToString(irisData));

        long start = System.currentTimeMillis();
        MultiThreadManager mtm = MultiThreadManager.getInstance();
        // maincode
        for (int i = 0; i < 100_000; i++) {
//            mtm.startResult(s100Data, tS100Data);
//            VectorSpaceHelper.multiplyTwoMatrices(s100Data, tS100Data);
        }
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
