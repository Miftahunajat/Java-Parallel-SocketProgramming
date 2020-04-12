package com.samples;

import com.Config;
import com.clustering.*;
import com.tunnel.Server;
import com.tunnel.ServerConfig;
import com.util.Core;

import java.util.Arrays;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        ServerConfig serverConfig = new ServerConfig(Config.PORT, Config.INET_ADDRESS_NAME);
        Server server = new Server(serverConfig);
        try {
            int[] results = server.hierarchicalClustering("src/main/resources/1kbigdata.csv", 3);
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
