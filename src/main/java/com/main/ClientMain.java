package com.main;


import com.Config;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
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
        Kryo kryo;
        kryo = new Kryo();
        Output output;
        Input input;
        while (true){
            try {
                int counter = 0;
                InetAddress ip = InetAddress.getByName(Config.INET_ADDRESS_NAME);
                Socket s = new Socket(ip, Config.PORT);

                ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());
                output = new Output(s.getOutputStream());
                kryo.register(Double[][].class);
                kryo.register(Double[].class);
                kryo.register(int.class);

                while (true) {
//                    System.out.println("waiting next line");
                    input = new Input(s.getInputStream());
                    counter++;
                    Object objData1;
                    Object objData2;
                    Double[][] data1 = null;
                    Double[][] data2 = null;
                    Integer firstLength = null;
                    Integer secondLength = null;

                    firstLength = kryo.readObject(input, int.class);
                    data1 = new Double[firstLength][];
                    for (Integer i = 0; i < firstLength; i++) {
                        data1[i] = kryo.readObject(input, Double[].class);
                    }

                    secondLength = kryo.readObject(input, int.class);
                    data2 = new Double[secondLength][];
                    for (Integer i = 0; i < secondLength; i++) {
                        data2[i] = kryo.readObject(input, Double[].class);
                    }

                    Double[][] hasil = VectorSpaceHelper.multiplyTwoMatrices(data1, data2);

                    int resultLength = hasil.length;
                    System.out.println("mau ngirim hasil");
                    kryo.writeObject(output, hasil);
                    output.flush();

//                    data1 = kryo.readObject(input, Double[][].class);
//                    System.out.println("read 1");
//                    System.out.println(Arrays.deepToString(data1));
//                    input = new Input(s.getInputStream());
//                    secondLength = (Integer) objectInputStream.readObject();
//                    data2 = new Double[secondLength][];
//                    for (Integer i = 0; i < secondLength; i++) {
//                        data2[i] = (Double[]) objectInputStream.readObject();
//                    }
//                    data2 = kryo.readObject(input, Double[][].class);
//                    System.out.println("read 2");
//                    System.out.println(Arrays.deepToString(data2));
//                    input.close();

//                    System.out.println(Arrays.deepToString(hasil));

//                    objectOutputStream.writeObject(hasil);


//            System.out.println(Arrays.deepToString(hasil));
                    System.out.println("Counter = " + counter);
//                    objectOutputStream.flush();

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


