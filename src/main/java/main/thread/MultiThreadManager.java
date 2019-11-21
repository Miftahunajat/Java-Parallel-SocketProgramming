package main.thread;

import main.Task;
import main.util.StringVector;
import main.util.VectorSpaceHelper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;


import static main.Config.MAX_CLIENT;
import static main.Config.PORT;

public class MultiThreadManager implements ClientHandler.ClientInteraction {

    public static int lastClientId = 0;
    private static Map<Integer, ClientHandler> threadClients = new HashMap<>();
    private ServerSocket serverSocket;
    private static MultiThreadManager instance;
    private AtomicIntegerArray clientStatuses = new AtomicIntegerArray(MAX_CLIENT);
    private int numberOfConnectedClient;
    private ExecutorService executorService;
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
                                double[][] results = VectorSpaceHelper.multiplyTwoMatrices(operation.getMatrixVector1(), operation.getMatrixVector2());
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
}