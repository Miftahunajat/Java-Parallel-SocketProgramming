package com.miftahunajat.thread;

import com.miftahunajat.clustering.CentroidDistance;
import com.miftahunajat.clustering.FutureCentroidDistance;
import com.miftahunajat.automatic.Statistic;
import com.miftahunajat.clustering.SerialHierarchicalClustering;
import com.miftahunajat.model.ClusterAndVariance;
import com.miftahunajat.model.ClusterAndVarianceFuture;
import com.miftahunajat.tunnel.ServerConfig;
import com.miftahunajat.util.VectorSpaceHelper;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class MultiThreadManager implements ClientHandler.ClientInteraction {

    public static int lastClientId = 0;
    private static Map<Integer, ClientHandler> threadClients = new HashMap<>();
    private ServerSocket serverSocket;
    private static MultiThreadManager instance;
    private AtomicIntegerArray clientStatuses;
    private int numberOfConnectedClient;
    public ExecutorService executorService;
    public AtomicInteger serverComputeCount = new AtomicInteger();
    public AtomicInteger temp = new AtomicInteger();
    public int[] clientComputeCount;

    private double[][] dataset;

    private MultiThreadManager(ServerConfig serverConfig) throws IOException {
        executorService = Executors.newFixedThreadPool(10);
        serverSocket = new ServerSocket(serverConfig.getPort());
        clientComputeCount = new int[serverConfig.getMaxClient()];
        clientStatuses = new AtomicIntegerArray(serverConfig.getMaxClient());
        awaitClient();
    }

    public static MultiThreadManager getInstance(ServerConfig serverConfig) throws IOException {
        if (instance == null) {
            instance = new MultiThreadManager(serverConfig);
        }
        return instance;
    }

    private void awaitClient() {
        new Thread(() -> {
            System.out.println("Connected Node : " + lastClientId);
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                ClientHandler clientHandler = null;
                clientHandler = new ClientHandler(socket, lastClientId, this);
                clientStatuses.set(numberOfConnectedClient, 1);
                numberOfConnectedClient++;
                threadClients.put(lastClientId, clientHandler);
                threadClients.get(lastClientId).start();
                lastClientId++;
                awaitClient();
            } catch (IOException e) {
                System.out.println("Connection Closed");
            }
        }).start();
    }

    public void close(){

        threadClients.forEach((integer, clientHandler) -> {
            updateClientStatus(integer, 0);
            clientHandler.status = 0;
            clientHandler.stop();
        });
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Connection Closed");
        }
        executorService.shutdownNow();
        instance = null;
//        serverSocket.close();
    }

    public synchronized AtomicIntegerArray getClientStatus(){
        return clientStatuses;
    }

    public void updateClientStatus(int clientId, int status){
        clientStatuses.set(clientId, status);
    }

    @Override
    public void onClientStopStartResult(int clientId, Double[][] mat1, Double[][] mat2) {
        updateClientStatus(clientId, 0);
        startResult(mat1,mat2);
    }

    @Override
    public void onClientStop(int clientId, double[][] mat1, double[][] mat2, double[] rangeI, double[] rangeJ) {

    }

    @Override
    public void onClientStop(int clientId, Double[][] mat1, Double[][] mat2) {

    }

    @Override
    public void onClientStopHandler(int clientId, Double[][] mat1, Double[][] mat2, Double[] rangeI, Double[] rangeJ) {
        updateClientStatus(clientId, 0);
        getDistanceMetric(mat1, mat2, rangeI, rangeJ);
    }

    @Override
    public void onClientStopDistances(int clientId, Double[] mat1, Double[] mat2) {
        updateClientStatus(clientId, 0);
        getDistance(mat1,mat2);
    }

    @Override
    public void onCLientWorking(int clientId) {
        updateClientStatus(clientId, 2);
    }

    @Override
    public void onClientFinished(int clientId) {
        updateClientStatus(clientId, 1);
    }

    @Override
    public void onClientStopStartResult(int clientId, int x) {
        sendClusters(this.dataset, x);
    }

    public Future<Double[][]> startResult(Double[][] mat1, Double[][] mat2){
//        boolean sent = false;
        for (int i = 0; i < clientStatuses.length(); i++) {
            if (clientStatuses.get(i) == 1){
//                sent = true;
                clientComputeCount[i]++;
                int finalI = i;
                onCLientWorking(i);
                return executorService.submit(new TaskFuture(mat1, mat2) {
                    @Override
                    public Double[][] call() {
//                        long start = System.currentTimeMillis();

                        Double[][] hasil = threadClients.get(finalI).sendTask(mat1, mat2);

//                        long finish = System.currentTimeMillis();
//                        long timeElapsed = finish - start;
//                        System.out.println("=========================== CLient");
//                        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
                        return hasil;
//                        return null;
                    }
                });
            }
//            if ( i == clientStatuses.length() - 1) temp.incrementAndGet();
        }
        try {
//            long start = System.currentTimeMillis();
            Double[][] results = VectorSpaceHelper.multiplyTwoMatrices(mat1, mat2);
//            long finish = System.currentTimeMillis();
//            long timeElapsed = finish - start;
//            System.out.println("=========================== Server");
//            System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
            serverComputeCount.incrementAndGet();
            return ConcurrentUtils.constantFuture(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Future<Double> getDistance(Double[] mat1, Double[] mat2){
//        boolean sent = false;
        for (int i = 0; i < clientStatuses.length(); i++) {
            if (clientStatuses.get(i) == 1){
//                sent = true;
                clientComputeCount[i]++;
                int finalI = i;
                onCLientWorking(i);
                return executorService.submit(new FutureGetDistance(mat1, mat2) {
                    @Override
                    public Double call() {
                        return threadClients.get(finalI).getDistanceTask(mat1, mat2);
//                        return null;
                    }
                });
            }
//            if ( i == clientStatuses.length() - 1) temp.incrementAndGet();
        }
        try {
            double results = VectorSpaceHelper.getDistances(mat1, mat2);
//            System.out.println("Server : 1" + results);
//            serverComputeCount.incrementAndGet();
            if (results == 0.0) System.out.println(results);
            return ConcurrentUtils.constantFuture(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Future<CentroidDistance> getCentroidDistance(Double[] mat1, Double[] mat2, int left, int right){
//        boolean sent = false;
        for (int i = 0; i < clientStatuses.length(); i++) {
            if (clientStatuses.get(i) == 1){
//                sent = true;
                clientComputeCount[i]++;
                int finalI = i;
                onCLientWorking(i);
                return executorService.submit(new FutureCentroidDistance(mat1, mat2,left, right) {
                    @Override
                    public CentroidDistance call() {
                        return new CentroidDistance(threadClients.get(finalI).getDistanceTask(mat1, mat2), left,right);
//                        return null;
                    }
                });
            }
//            if ( i == clientStatuses.length() - 1) temp.incrementAndGet();
        }
        try {
            double results = VectorSpaceHelper.getDistances(mat1, mat2);
//            System.out.println("Server : 1" + results);
//            serverComputeCount.incrementAndGet();
            if (results == 0.0) System.out.println(results);
            CentroidDistance centroidDistance = new CentroidDistance(results, left, right);
            return ConcurrentUtils.constantFuture(centroidDistance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Future<Double[][]> getDistanceMetric(
            Double[][] dataRange1, Double[][] dataRange2,
            Double[] rangeI, Double[] rangeJ
    ) {
        for (int i = 0; i < clientStatuses.length(); i++) {
            if (clientStatuses.get(i) == 1) {
//                sent = true;
                clientComputeCount[i]++;
                int finalI = i;
                onCLientWorking(i);
                return executorService.submit(new FutureDistanceMetric(dataRange1, dataRange2, rangeI, rangeJ) {
                    @Override
                    public Double[][] call() {
//                        long start = System.currentTimeMillis();
                        Double[] distances = threadClients.get(finalI).getDistanceMetricTask(
                                mat1,
                                mat2);
                        long finish = System.currentTimeMillis();

                        Double[][] results = new Double[distances.length][3];
                        for (int i = 0; i < results.length; i++) {
                            results[i][0] = distances[i];
                            results[i][1] = this.rangeI[i];
                            results[i][2] = this.rangeJ[i];
                        }


//                        long timeElapsed = finish - start;
//                        System.out.println("=========================== CLient");
//                        System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
                        return results;
                    }
                });
            }
        }
        try {
//            long start = System.currentTimeMillis();
            Double[][] substractsResult = VectorSpaceHelper.substractTwoMatrices(dataRange1, dataRange2);
            Double[][] results = new Double[substractsResult.length][];
            for (int j = 0; j < substractsResult.length; j++) {
                Double res = 0.0;

                for (int k = 0; k < substractsResult[j].length; k++) {
                    res += substractsResult[j][k]*substractsResult[j][k];
                }
                results[j] = new Double[]{res, rangeI[j], rangeJ[j]};
            }
            Thread.sleep(36);

            long finish = System.currentTimeMillis();
//            long timeElapsed = finish - start;
//            System.out.println("=========================== PUsat");
//            System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
            return ConcurrentUtils.constantFuture(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Future<Double[][]> getDistanceMetricMain(Double[][] dataRange1, Double[][] dataRange2, Double[] rangeI, Double[] rangeJ) {
        try {
//            long start = System.currentTimeMillis();
            Double[][] substractsResult = VectorSpaceHelper.substractTwoMatrices(dataRange1, dataRange2);
            Double[][] results = new Double[substractsResult.length][];
            for (int j = 0; j < substractsResult.length; j++) {
                Double res = 0.0;

                for (int k = 0; k < substractsResult[j].length; k++) {
                    res += substractsResult[j][k]*substractsResult[j][k];
                }
                results[j] = new Double[]{res, rangeI[j], rangeJ[j]};
            }

//            long finish = System.currentTimeMillis();
//            long timeElapsed = finish - start;
//            System.out.println("=========================== CLient");
//            System.out.println("Time Elapsed : " + timeElapsed/1000.f + "Seconds");
            return ConcurrentUtils.constantFuture(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Future<CentroidDistance> getCentroidDistanceMain(Double[] mat1, Double[] mat2, int left, int right){
        try {
            double results = VectorSpaceHelper.getDistances(mat1, mat2);
//            System.out.println("Server : 1" + results);
//            serverComputeCount.incrementAndGet();
            if (results == 0.0) System.out.println(results);
            CentroidDistance centroidDistance = new CentroidDistance(results, left, right);
            return ConcurrentUtils.constantFuture(centroidDistance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Future<ClusterAndVariance> sendClusters(double[][] data, int x) {
        //        boolean sent = false;
        for (int i = 0; i < clientStatuses.length(); i++) {
            if (clientStatuses.get(i) == 1){
//                sent = true;
                clientComputeCount[i]++;
                int finalI = i;
                onCLientWorking(i);
                return executorService.submit(new ClusterAndVarianceFuture(){
                    @Override
                    public ClusterAndVariance call() throws Exception {
                        return threadClients.get(finalI).getClusterAndVariance(x);
                    }
                });
            }
//            if ( i == clientStatuses.length() - 1) temp.incrementAndGet();
        }
        try {
            int[] clustersResult = SerialHierarchicalClustering.pCentroidLinkageClustering(data,x);
            double[] variance = new Statistic().getVariance(data, clustersResult);
            ClusterAndVariance clusterAndVariance = new ClusterAndVariance(clustersResult, variance);
            return ConcurrentUtils.constantFuture(clusterAndVariance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}