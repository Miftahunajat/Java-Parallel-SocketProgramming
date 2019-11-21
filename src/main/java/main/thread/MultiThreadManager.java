package main.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerArray;


import foo.Task;

import static foo.Config.*;

public class MultiThreadManager implements ClientHandler.ClientInteraction {

    public static int lastClientId = 0;
    private static Map<Integer, ClientHandler> threadClients = new HashMap<>();
    private ServerSocket serverSocket;
    private static MultiThreadManager instance;
    private AtomicIntegerArray clientStatuses = new AtomicIntegerArray(MAX_CLIENT);
    private int numberOfConnectedClient;
    private ExecutorService executorService;

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
                clientStatuses.set(numberOfConnectedClient, 1);
                numberOfConnectedClient++;
            } catch (IOException e) {
                e.printStackTrace();
            }
            ClientHandler clientHandler = new ClientHandler(socket, lastClientId, this);
            threadClients.put(lastClientId, clientHandler);
            threadClients.get(lastClientId).start();
            lastClientId++;
            awaitClient();
        }).start();
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
                        break;
                    }
                }
                if (!sent){
                    executorService.execute(new Task(input) {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3_000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Server : " + taskToSent);
                        }
                    });

                }
            }
        }).start();
        /*executorService.execute(new Task(input) {
            @Override
            public void run() {

            }
        });*/
    }
}