package com.main;

import com.Config;
import com.automatic.AutomaticClustering;
import com.clustering.ML;
import com.clustering.SerialHierarchicalClustering;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.util.Core;
import com.util.ThreadUtil;
import com.util.VectorSpaceHelper;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ClientAutomaticClusteringMain {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        double[][] datas = Core.readLargeCSV(Config.fileLocation);
        int counter = 0;
        while (true){
            try {
                InetAddress ip = InetAddress.getByName(Config.INET_ADDRESS_NAME);
                Socket s = new Socket(ip, Config.PORT);

                DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());

                boolean connected = true;

                while (connected) {
                    counter++;
                    int numClusters = (int) ThreadUtil.readObjectFromStream(dataInputStream);
                    int[] cluster = SerialHierarchicalClustering.pCentroidLinkageClustering(datas, numClusters);
                    double[] variances = new ML().getVariance(datas, cluster);

                    ThreadUtil.writeObjectToStream(dataOutputStream, cluster);
                    ThreadUtil.writeObjectToStream(dataOutputStream, variances);

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
