package com.miftahunajat.tunnel;

import com.miftahunajat.Config;
import com.miftahunajat.automatic.AutomaticClustering;
import com.miftahunajat.automatic.Statistic;
import com.miftahunajat.clustering.ParallelHierarchicalClustering;
import com.miftahunajat.clustering.SerialHierarchicalClustering;
import com.miftahunajat.util.Core;
import com.miftahunajat.util.ThreadUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private ClientConfig clientConfig;

    public Client(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public void connect() throws IOException, InterruptedException, ClassNotFoundException {
        double[][] datas = Core.readLargeCSV(Config.fileLocation);
        int counter = 0;
        while (true){
            try {
                InetAddress ip = InetAddress.getByName(clientConfig.getInetAddress());
                Socket s = new Socket(ip, clientConfig.getPort());

                DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());

                boolean connected = true;

                while (connected) {
                    counter++;
                    int numClusters = (int) ThreadUtil.readObjectFromStream(dataInputStream);
                    int[] cluster = SerialHierarchicalClustering.pCentroidLinkageClustering(datas, numClusters);
                    double[] variances = new Statistic().getVariance(datas, cluster);

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

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }
}
