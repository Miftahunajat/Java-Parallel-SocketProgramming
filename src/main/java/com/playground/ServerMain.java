package com.playground;


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

        double[][] mat1 = new com.bayudwiyansatria.mat.Mat().initArrayRandom(10,1000,1,1000.0);
        double[][] mat2 = new com.bayudwiyansatria.mat.Mat().initArrayRandom(1000,10_000,1,1000.0);

        Double[][] kmat1 = new Double[][]{{1.0,2.0}, {1.0,2.0}};
        Double[][] kmat2 = new Double[][]{{2.0,3.0}, {2.0,3.0}};
        Double[][] hasil = VectorSpaceHelper.multiplyTwoMatrices(kmat1, kmat2);
        System.out.println(Arrays.deepToString(hasil));

        Double[][] mat1O = Arrays.stream(new com.bayudwiyansatria.mat.Mat().initArrayRandom(10,1000,1,1000.0)).map(ArrayUtils::toObject).toArray(Double[][]::new);
        Double[][] mat2O = Arrays.stream(new com.bayudwiyansatria.mat.Mat().initArrayRandom(1000,10_000,1,1000.0)).map(ArrayUtils::toObject).toArray(Double[][]::new);
//        new com.bayudwiyansatria.io.IO().saveCSV(mat2, "matrix2");

        System.out.println("aggregate client");
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

        MultiThreadManager mtm = MultiThreadManager.getInstance();
        for (int i = 0; i < 60; i++) {
//            System.out.println(i);
//            mtm.startResult(
//                    Arrays.deepToString(mat1) + "||*||" + Arrays.deepToString(mat2)
//            );
//            System.out.println(i);
            hasilPerhitungan.add(mtm.startResult(mat1O, mat2O));
//
//            System.out.println(mtm.getClientStatus());

//            objectOutputStream.writeObject(mat1O);
//            objectOutputStream.writeObject(mat2O);
//            System.out.println(i);
//            Double[][] sHasil = (Double[][]) objectInputStream.readObject();
//            System.out.println(Arrays.deepToString(sHasil));
        }
        hasilPerhitungan.forEach(a -> {
            try {
                a.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
        System.out.println("Server Compute Count : " + mtm.serverComputeCount.get());
        System.out.println("Client Compute Count: " + mtm.clientComputeCount.toString());
        System.out.println("Temp Compute Count: " + mtm.temp.get());
//        objectOutputStream.flush();
//        objectOutputStream.close();
    }
}
