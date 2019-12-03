package com.playground;


import com.thread.MultiThreadManager;
import com.util.VectorSpaceHelper;
import org.apache.commons.lang3.ArrayUtils;
import sun.security.util.ArrayUtil;

import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.Config.PORT;


public class ServerMain {

    private static ServerSocket serverSocket;
    private static Socket socket;

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        double[][] mat1 = new com.bayudwiyansatria.mat.Mat().initArrayRandom(10,1000,1,1000.0);
        double[][] mat2 = new com.bayudwiyansatria.mat.Mat().initArrayRandom(1000,10_000,1,1000.0);

        Double[][] dummer = new Double[][]{{1.0,2.0,3.0}, {1.0,2.0,3.0}, {1.0,2.0,3.0}};

        Double[][] mat1O = Arrays.stream(new com.bayudwiyansatria.mat.Mat().initArrayRandom(10,1000,1,1000.0)).map(ArrayUtils::toObject).toArray(Double[][]::new);
        Double[][] mat2O = Arrays.stream(new com.bayudwiyansatria.mat.Mat().initArrayRandom(1000,10_000,1,1000.0)).map(ArrayUtils::toObject).toArray(Double[][]::new);

        serverSocket = new ServerSocket(PORT);
        System.out.println("aggregate client");
        socket = serverSocket.accept();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        for (int i = 0; i < 60; i++) {
            System.out.println(i);
            objectOutputStream.writeObject(dummer);
            System.out.println(i);
        }
        while (true){
        }
//        long finish = System.currentTimeMillis();
//        long timeElapsed = finish - start;
//        System.out.println("===========================");
//        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
    }
}
