package com.thread;

import com.Task;
import com.TaskFuture;
import com.util.Core;
import com.util.StringVector;
import com.util.VectorSpaceHelper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
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
    public AtomicIntegerArray clientComputeCount = new AtomicIntegerArray(MAX_CLIENT);

    private MultiThreadManager() throws IOException {
        executorService = Executors.newFixedThreadPool(10);
        serverSocket = new ServerSocket(PORT);
        awaitClient();
//        checkThreadStatuses();
    }

    public static MultiThreadManager getInstance() throws IOException {
        if (instance == null) {
            instance = new MultiThreadManager();
        }
        return instance;
    }

    private void awaitClient(){
        new Thread(() -> {
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
                e.printStackTrace();
            }

        }).start();
    }

    public void close(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized AtomicIntegerArray getClientStatus(){
        return clientStatuses;
    }

    public synchronized void updateClientStatus(int clientId, int status){
        clientStatuses.set(clientId, status);
    }

    private void checkThreadStatuses(){
        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadClients.forEach((integer, clientHandler) -> {
//                clientStatuses[integer] = clientHandler.status;
            });
            checkThreadStatuses();
        }).start();
    }

    @Override
    public void onClientStop(int clientId, String failedInput) {
        updateClientStatus(clientId, 0);
        startResult(failedInput);
    }

    @Override
    public void onClientStop(int clientId, Double[][] mat1, Double[][] mat2) {
        updateClientStatus(clientId, 0);
        startResult(mat1,mat2);
    }

    @Override
    public void onCLientWorking(int clientId) {
        updateClientStatus(clientId, 2);
    }

    @Override
    public void onClientFinished(int clientId) {
        updateClientStatus(clientId, 1);
    }


    public void startResult(String input){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean sent = false;
                for (int i = 0; i < clientStatuses.length(); i++) {
                    if (clientStatuses.get(i) == 1){
                        sent = true;
                        threadClients.get(i).sendTask(input);
                        clientComputeCount.incrementAndGet(i);
                        break;
                    }
                    if ( i == clientStatuses.length() - 1) temp.incrementAndGet();
                }
                if (!sent){
                    executorService.execute(new Task(input) {
                        @Override
                        public void run() {
                            StringVector operation = new StringVector(taskToSent, true);
                            operation.getMatrixVector1();
                            operation.getMatrixVector2();
                            try {
                                double[][] results = Core.getMatInstance().vectorMultiplication(operation.getMatrixVector1(), operation.getMatrixVector2());
                                System.out.println("Server : " + Arrays.deepToString(results));
                                serverComputeCount.incrementAndGet();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        }).start();
    }


    public Future<?> startResult(Double[][] mat1, Double[][] mat2){
        boolean sent = false;
        for (int i = 0; i < clientStatuses.length(); i++) {
            if (clientStatuses.get(i) == 1){
                sent = true;
                clientComputeCount.incrementAndGet(i);
                int finalI = i;
                return executorService.submit(new TaskFuture(mat1, mat2) {
                    @Override
                    public Double[][] call() throws Exception {
                        threadClients.get(finalI).sendTask(mat1, mat2);
                        return new Double[0][];
                    }
                });
            }
            if ( i == clientStatuses.length() - 1) temp.incrementAndGet();
        }
        if (!sent){
            try {
                Double[][] results = VectorSpaceHelper.multiplyTwoMatrices(mat1, mat2);
//                System.out.println("Server : " + Arrays.deepToString(results));
                System.out.println("Server : 1");
                serverComputeCount.incrementAndGet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
//        return executorService.submit(new TaskFuture(mat1, mat2) {
//            @Override
//            public Double[][] call() {
//
//            }
//        });
    }
}