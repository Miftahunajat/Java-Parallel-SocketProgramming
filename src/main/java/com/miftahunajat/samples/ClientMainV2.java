package com.miftahunajat.samples;


import com.miftahunajat.Config;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.miftahunajat.util.VectorSpaceHelper;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientMainV2 {
    public static void main(String[] args) throws Exception {
        Kryo kryo;
        kryo = new Kryo();
        Output output;
        Input input;
        int counter = 0;
        while (true){
            try {
                InetAddress ip = InetAddress.getByName(Config.INET_ADDRESS_NAME);
                Socket s = new Socket(ip, Config.PORT);

                ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());
                output = new Output(s.getOutputStream());
                kryo.register(Double[].class);
                kryo.register(Double.class);
                kryo.register(int.class);

                boolean connected = true;

                while (connected) {
                    input = new Input(s.getInputStream());
                    counter++;
                    Object objData1;
                    Object objData2;
                    Double[] data1 = null;
                    Double[] data2 = null;
                    Integer firstLength = null;
                    Integer secondLength = null;

                    firstLength = kryo.readObject(input, int.class);
                    data1 = new Double[firstLength];
                    for (Integer i = 0; i < firstLength; i++) {
                        data1[i] = kryo.readObject(input, Double.class);
                    }

                    secondLength = kryo.readObject(input, int.class);
                    data2 = new Double[secondLength];
                    for (Integer i = 0; i < secondLength; i++) {
                        data2[i] = kryo.readObject(input, Double.class);
                    }

                    Double hasil = VectorSpaceHelper.getDistances(data1, data2);


                    kryo.writeObject(output, hasil);
                    output.flush();


                }
            }catch (ConnectException connectException){
                connectException.printStackTrace();
                System.out.println("Server not found");
                System.out.println("Reconnecting . . .");
                Thread.sleep(5_000);
            } finally {
                System.out.println("Counter = " + counter);
            }
        }
    }
}


