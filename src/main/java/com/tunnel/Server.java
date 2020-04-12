package com.tunnel;

import com.clustering.ParallelHierarchicalClustering;
import com.util.Core;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

public class Server {
    private ServerConfig serverConfig;

    public Server(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public int[] hierarchicalClustering(String csv, int numbersOfClusters) {
        Double[][] datas = Core.readLargeCSVWrapper("src/main/resources/1kbigdata.csv");
        int[] results = null;
        try {
            results = ParallelHierarchicalClustering.centroidLinkageClustering(datas, numbersOfClusters);
        } catch (InterruptedException | IOException e) {
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
