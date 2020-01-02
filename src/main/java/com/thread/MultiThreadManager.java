package com.thread;

import com.util.VectorSpaceHelper;
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


import static com.Config.MAX_CLIENT;
import static com.Config.PORT;

public class MultiThreadManager implements ClientHandler.ClientInteraction {

    public static int lastClientId = 0;
    private static Map<Integer, ClientHandler> threadClients = new HashMap<>();
    private ServerSocket serverSocket;
    private static MultiThreadManager instance;
    private AtomicIntegerArray clientStatuses = new AtomicIntegerArray(MAX_CLIENT);
    private int numberOfConnectedClient;
    public ExecutorService executorService;
    public AtomicInteger serverComputeCount = new AtomicInteger();
    public AtomicInteger temp = new AtomicInteger();
    public int[] clientComputeCount = new int[MAX_CLIENT];

    private MultiThreadManager() throws IOException {
        executorService = Executors.newFixedThreadPool(10);
        serverSocket = new ServerSocket(PORT);
        awaitClient();
    }

    public static MultiThreadManager getInstance() throws IOException {
        if (instance == null) {
            instance = new MultiThreadManager();
        }
        return instance;
    }

    private void awaitClient() {
        executorService.execute(() -> {
            System.out.println("Waiting For Client number : " + lastClientId);
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
        });
    }

    public void close(){
        executorService.shutdown();
        threadClients.forEach((integer, clientHandler) -> {
            updateClientStatus(integer, 0);
        });
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Connection Closed");
        }
//        serverSocket.close();
    }

    public synchronized AtomicIntegerArray getClientStatus(){
        return clientStatuses;
    }

    public synchronized void updateClientStatus(int clientId, int status){
        clientStatuses.set(clientId, status);
    }

    @Override
    public void onClientStop(int clientId, Double[][] mat1, Double[][] mat2) {
        updateClientStatus(clientId, 0);
        startResult(mat1,mat2);
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
                        return threadClients.get(finalI).sendTask(mat1, mat2);
//                        return null;
                    }
                });
            }
//            if ( i == clientStatuses.length() - 1) temp.incrementAndGet();
        }
        try {
            Double[][] results = VectorSpaceHelper.multiplyTwoMatrices(mat1, mat2);
            System.out.println("Server : 1");
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
            System.out.println("Server : 1");
            serverComputeCount.incrementAndGet();
            return ConcurrentUtils.constantFuture(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}