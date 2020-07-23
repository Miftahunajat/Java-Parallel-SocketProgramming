package com.miftahunajat.tunnel;

public class ClientConfig {
    private int port;
    private String inetAddress;

    public ClientConfig(int port, String inetAddress) {
        this.port = port;
        this.inetAddress = inetAddress;
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
}
