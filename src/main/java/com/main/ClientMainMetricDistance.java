package com.main;


import com.Config;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.util.ThreadUtil;
import com.util.VectorSpaceHelper;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientMainMetricDistance {


    public static void main(String[] args) throws Exception {
        Kryo kryo;
        kryo = new Kryo();
        Output output;
        Input input;
        int counter = 0;
        FSTConfiguration fstConfiguration;
        FSTObjectOutput fOutput;
        FSTObjectInput fInput;
        while (true){
            try {
                InetAddress ip = InetAddress.getByName(Config.INET_ADDRESS_NAME);
                Socket s = new Socket(ip, Config.PORT);

                DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());

                kryo.register(double[].class);
                kryo.register(Double[].class);
                kryo.register(Double.class);
                kryo.register(int.class);

                boolean connected = true;

                while (connected) {
                    input = new Input(s.getInputStream());
                    counter++;
                    Object objData1;
                    Double[][] data1 = null;
                    Double[][] data2 = null;

                    data1 = (Double[][]) ThreadUtil.readObjectFromStream(dataInputStream);
                    data2 = (Double[][]) ThreadUtil.readObjectFromStream(dataInputStream);

                    Double[][] substractsResult = VectorSpaceHelper.substractTwoMatrices(data1, data2);
                    Double[] hasil = new Double[substractsResult.length];
                    for (int j = 0; j < substractsResult.length; j++) {
                        Double res = 0.0;
                        for (int k = 0; k < substractsResult[j].length; k++) {
                            res += substractsResult[j][k]*substractsResult[j][k];
                        }
                        hasil[j] = res;
                    }


                    int resultLength = hasil.length;
                    ThreadUtil.writeObjectToStream(dataOutputStream, hasil);

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


