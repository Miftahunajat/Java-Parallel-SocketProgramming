package com.main;

import com.thread.MultiThreadManager;
import com.util.Core;

import java.io.IOException;
import java.util.Arrays;

public class ServerMainIris {
    public static void main(String[] args) throws IOException {
        double[][] irisData = Core.getIoInstance().readCSV_double("iris");
        System.out.println(Arrays.deepToString(irisData));

        long start = System.currentTimeMillis();
        // maincode
        MultiThreadManager mtm = MultiThreadManager.getInstance();
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
