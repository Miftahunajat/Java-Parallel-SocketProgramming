package com.thread;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

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



    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;


    public ClientHandler(Socket socket, int clientId, ClientInteraction clientInteraction) throws IOException {
        System.out.println("Waiting CLient " + clientId + " TO CONNECT");
        this.clientId = clientId;
        this.socket = socket;
        this.clientInteraction = clientInteraction;
        status = 1;
        kryo = new Kryo();
        kryo.register(Double[][].class);
        input = new Input(socket.getInputStream());
        output = new Output(socket.getOutputStream());


        try {
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
//            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
//            String connected = "Client number" + clientId + " is connected\n";
//            dos.writeUTF(connected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        while (status != 0) {}
    }

    public Double[][] sendTask(Double[][] mat1, Double[][] mat2) {
        synchronized (this) {
            try {
                kryo.writeObject(output, mat1);
                kryo.writeObject(output, mat2);
                output.flush();

                Double[][] hasil = null;
                hasil = kryo.readObject(input, Double[][].class);
                System.out.println("Client " + clientId + ": ");
                clientInteraction.onClientFinished(clientId);
                return hasil;
            } catch (Exception e) {
                clientInteraction.onClientStop(clientId, mat1, mat2);
//            status = 0;
//            e.printStackTrace();
            } finally {
                return null;
            }
        }

//        fst
//        byte barray[] = conf.asByteArray(mat1);
//        dos.writeInt(barray.length);
//        dos.write(barray);

    }



    interface ClientInteraction{
        void onClientStop(int clientId, Double[][] mat1, Double[][] mat2);
        void onCLientWorking(int clientId);
        void onClientFinished(int clientId);
    }
}