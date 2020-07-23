package com.miftahunajat.tunnel;

import com.miftahunajat.automatic.AutomaticClustering;
import com.miftahunajat.clustering.ParallelHierarchicalClustering;
import com.miftahunajat.util.Core;

import java.io.IOException;

public class Server {
    private ServerConfig serverConfig;

    public Server(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public int[] hierarchicalClustering(String csv, int numbersOfClusters) {
        Double[][] datas = Core.readLargeCSVWrapper(csv);
        int[] results = null;
        try {
            results = ParallelHierarchicalClustering.centroidLinkageClustering(datas, numbersOfClusters, serverConfig);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    public int[] automaticClustering(String csv, int interval) {
        double[][] datas = Core.readLargeCSV(csv);
        int[] results = null;
        try {
            results = new AutomaticClustering().parallelHierarchicalAutomaticClustering(datas, interval, serverConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
}
