package com.thread;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.util.ThreadUtil;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler extends Thread
{
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket socket;
    private int clientId;
    private ClientInteraction clientInteraction;
    int status;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private Kryo kryo;
    private Output output;
    private Input input;

    private FSTConfiguration fstConfiguration;
    private FSTObjectOutput fOutput;
    private FSTObjectInput fInput;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;


    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;


    public ClientHandler(Socket socket, int clientId, ClientInteraction clientInteraction) throws IOException {
        System.out.println("Waiting CLient " + clientId + " TO CONNECT");
        this.clientId = clientId;
        this.socket = socket;
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.clientInteraction = clientInteraction;
        status = 1;
        fstConfiguration = FSTConfiguration.createDefaultConfiguration();
        fstConfiguration.registerClass(Double[][].class);
        fstConfiguration.registerClass(Double[].class);
//        fOutput = new FSTObjectOutput(socket.getOutputStream());
//        fInput = new FSTObjectInput(socket.getInputStream());


        kryo = new Kryo();
        kryo.register(Double[][].class);
        kryo.register(double[].class);
        kryo.register(Double[].class);
        kryo.register(Double.class);
        kryo.register(int.class);



//        input = new Input(socket.getInputStream());
//        output = new Output(socket.getOutputStream());


//        try {
//            this.dis = new DataInputStream(socket.getInputStream());
//            this.dos = new DataOutputStream(socket.getOutputStream());
//            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
//            String connected = "Client number" + clientId + " is connected\n";
//            dos.writeUTF(connected);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void run()
    {
        while (status != 0) {
            if (status == 0) System.out.println("tralalalalala");
        }
    }

    public Double[][] sendTask(Double[][] mat1, Double[][] mat2) {
        synchronized (this) {
            try {
                ThreadUtil.writeObjectToStream(dataOutputStream, mat1);
                ThreadUtil.writeObjectToStream(dataOutputStream, mat2);
                Double[][] hasil = (Double[][]) ThreadUtil.readObjectFromStream(dataInputStream);

//                System.out.println("Client " + clientId + ": " + hasil);
                clientInteraction.onClientFinished(clientId);
                return hasil;
            } catch (Exception e) {
                clientInteraction.onClientStopStartResult(clientId, mat1, mat2);
            } finally {
                return null;
            }
        }
    }

    public Double[] getDistanceMetricTask(Double[][] mat1, Double[][] mat2) {
//        synchronized (this) {
            try {
                ThreadUtil.writeObjectToStream(dataOutputStream, mat1);
                ThreadUtil.writeObjectToStream(dataOutputStream, mat2);

                Double[] hasil = (Double[]) ThreadUtil.readObjectFromStream(dataInputStream);
                clientInteraction.onClientFinished(clientId);
                return hasil;
            } catch (Exception e) {
                clientInteraction.onClientStop(clientId, mat1, mat2);
                return null;
            }
//        }
    }

    public double getDistanceTask(Double[] mat1, Double[] mat2) {
        synchronized (this) {
            try {
                kryo.writeObject(output, mat1.length);
                for (int i = 0; i < mat1.length; i++) {
                    kryo.writeObject(output, mat1[i]);
                }

                kryo.writeObject(output, mat2.length);
                for (int i = 0; i < mat2.length; i++) {
                    kryo.writeObject(output, mat2[i]);
                }
                output.flush();

                Double hasil;
                hasil = kryo.readObject(input, Double.class);

//                System.out.println("Client " + clientId + ": " + hasil);
                clientInteraction.onClientFinished(clientId);
                if (hasil == 0.0) System.out.println(hasil);
                return hasil;
            } catch (Exception e) {
                e.printStackTrace();
                clientInteraction.onClientStopDistances(clientId, mat1, mat2);
                return Double.MAX_VALUE;
            }
        }

    }

    public void disconnect() {
        kryo.writeObject(output, -1);
    }


    interface ClientInteraction{
        void onClientStop(int clientId, Double[][] mat1, Double[][] mat2);
        void onClientStopHandler(int clientId, Double[][] mat1, Double[][] mat2, Double[] rangeI, Double[] rangeJ);
//        void onClientStop(int clientId, Double[][] mat1, Double[][] mat2);
        void onClientStopStartResult(int clientId, Double[][] mat1, Double[][] mat2);
        void onClientStop(int clientId, double[][] mat1, double[][] mat2, double[] rangeI, double[] rangeJ);
        void onClientStopDistances(int clientId, Double[] mat1, Double[] mat2);
        void onCLientWorking(int clientId);
        void onClientFinished(int clientId);

//        void onClientStop(int clientId, Double[][] mat1);
    }
}