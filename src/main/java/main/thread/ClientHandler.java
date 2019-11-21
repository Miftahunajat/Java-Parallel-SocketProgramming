package main.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

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


    public ClientHandler(Socket socket, int clientId, ClientInteraction clientInteraction)  {
        System.out.println("Waiting CLient " + clientId + " TO CONNECT");
        this.clientId = clientId;
        this.socket = socket;
        this.clientInteraction = clientInteraction;
        status = 1;

        try {
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String connected = "Client number" + clientId + " is connected\n";
            dos.writeUTF(connected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        while (status != 0)
        {
//            try {
//                Thread.sleep(5000);
//                dos.write("check status \n".getBytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println(e.toString());
//                clientInteraction.onClientStop(clientId);
//                status = 0;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            try {
//
//                // Ask user what he wants
//                dos.writeUTF("What do you want?[Date | Time]..\n"+
//                        "Type Exit to terminate connection.");
//
//                // receive the answer from client
//                received = dis.readUTF();
//
//                if(received.equals("Exit"))
//                {
////                    System.out.println("Client " + this.s + " sends exit...");
//                    System.out.println("Closing this connection.");
////                    this.s.close();
//                    System.out.println("Connection closed");
//                    break;
//                }
//
//                // creating Date object
//                Date date = new Date();
//
//                // write on output stream based on the
//                // answer from the client
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

//        try
//        {
            // closing resources
//            this.dis.close();
//            this.dos.close();
//
//        }catch(IOException e)
//        {
//            e.printStackTrace();
//        }
    }

    public void sendTask(String input) {
        clientInteraction.onCLientWorking(clientId);
        try {
//            System.out.println("Send Task");
            dos.write((input + "\n").getBytes());
//            System.out.println("Task Sent");
            String hasil = bufferedReader.readLine();
//            System.out.println("Task Completed");
            System.out.println(hasil);
            clientInteraction.onClientFinished(clientId);
        } catch (IOException e) {
            clientInteraction.onClientStop(clientId, input);
//            status = 0;
//            e.printStackTrace();
        } finally {

        }

    }

    interface ClientInteraction{
        void onClientStop(int clientId, String failedInput);
        void onCLientWorking(int clientId);
        void onClientFinished(int clientId);
    }
}