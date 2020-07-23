package com.miftahunajat.tunnel;

public class ServerConfig {
    private int port;
    private String inetAddress;
    private int maxClient;

    public ServerConfig(int port, String inetAddress) {
        this.port = port;
        this.inetAddress = inetAddress;
        this.maxClient = 8;
    }

    public ServerConfig(int port, String inetAddress, int maxClient) {
        this.port = port;
        this.inetAddress = inetAddress;
        this.maxClient = maxClient;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(String inetAddress) {
        this.inetAddress = inetAddress;
    }

    public int getMaxClient() {
        return maxClient;
    }

    public void setMaxClient(int maxClient) {
        this.maxClient = maxClient;
    }
}
