package com.miftahunajat.samples;

import com.miftahunajat.Config;
import com.miftahunajat.automatic.Statistic;
import com.miftahunajat.clustering.SerialHierarchicalClustering;
import com.miftahunajat.tunnel.Client;
import com.miftahunajat.tunnel.ClientConfig;
import com.miftahunajat.tunnel.Server;
import com.miftahunajat.tunnel.ServerConfig;
import com.miftahunajat.util.Core;
import com.miftahunajat.util.ThreadUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class AutomaticClusteringClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        long start = System.currentTimeMillis();
        ClientConfig clientConfig = new ClientConfig(Config.PORT, Config.INET_ADDRESS_NAME);
        Client client = new Client(clientConfig);
        try {
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
    }
}
