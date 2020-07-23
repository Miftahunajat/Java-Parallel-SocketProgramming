package com.miftahunajat.samples;

import com.miftahunajat.Config;
import com.miftahunajat.tunnel.Server;
import com.miftahunajat.tunnel.ServerConfig;

import java.util.Arrays;

public class AutomaticClusteringServer {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ServerConfig serverConfig = new ServerConfig(Config.PORT, Config.INET_ADDRESS_NAME);
        Server server = new Server(serverConfig);
        try {
            int[] results = server.automaticClustering(Config.fileLocation, Config.INTERVAL);
            System.out.println(Arrays.toString(results));
        } catch (Exception e) {
            e.printStackTrace();
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("===========================");
        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");



    }
}
