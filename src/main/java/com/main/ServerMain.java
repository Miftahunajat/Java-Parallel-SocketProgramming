package com.main;


import com.thread.MultiThreadManager;
import com.util.VectorSpaceHelper;
import org.apache.commons.lang3.ArrayUtils;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ServerMain {

    private static ServerSocket serverSocket;
    private static Socket socket;

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        List<Future<Double[][]>> hasilPerhitungan = new ArrayList<>();

        Double[][] kmat1 = new Double[][]{{1.0,2.0}, {1.0,2.0}};
        Double[][] kmat2 = new Double[][]{{2.0,3.0}, {2.0,3.0}};
        Double[][] hasil = VectorSpaceHelper.multiplyTwoMatrices(kmat1, kmat2);

        Double[][] mat1O = Arrays.stream(new com.bayudwiyansatria.mat.Mat().initArrayRandom(10,1000,1,1000.0)).map(ArrayUtils::toObject).toArray(Double[][]::new);
        Double[][] mat2O = Arrays.stream(new com.bayudwiyansatria.mat.Mat().initArrayRandom(1000,10_000,1,1000.0)).map(ArrayUtils::toObject).toArray(Double[][]::new);

        MultiThreadManager mtm = MultiThreadManager.getInstance();
        for (int i = 0; i < 25; i++) {
            hasilPerhitungan.add(mtm.startResult(mat1O, mat2O));
        }
        hasilPerhitungan.forEach(a -> {
            try {
                a.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        mtm.close();

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
        System.out.println("Server Compute Count : " + mtm.serverComputeCount.get());
        System.out.println("Client Compute Count: " + Arrays.toString(mtm.clientComputeCount));
        System.out.println("Temp Compute Count: " + mtm.temp.get());
    }
}
