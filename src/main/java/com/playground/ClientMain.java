package com.playground;


import com.Config;
import com.util.Core;
import com.util.StringVector;
import com.util.VectorSpaceHelper;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        while (true){
            try {
                int counter = 0;

                InetAddress ip = InetAddress.getByName(Config.INET_ADDRESS_NAME);
                Socket s = new Socket(ip, Config.PORT);

                ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());

                while (true) {
                    System.out.println("waiting next line");
                    counter++;
                    Object objData1;
                    Object objData2;
                    Double[][] data1 = null;
                    Double[][] data2 = null;
                    data1 = (Double[][]) objectInputStream.readObject();
                    data2 = (Double[][]) objectInputStream.readObject();

                    Double[][] hasil = VectorSpaceHelper.multiplyTwoMatrices(data1, data2);
                    System.out.println(Arrays.deepToString(hasil));
                    objectOutputStream.writeObject(hasil);

//            System.out.println(Arrays.deepToString(hasil));
                    System.out.println("Counter = " + counter);

                }
            }catch (ConnectException connectException){
                connectException.printStackTrace();
                System.out.println("Server not found");
                System.out.println("Reconnecting . . .");
                Thread.sleep(5_000);
            }
        }
    }
}


