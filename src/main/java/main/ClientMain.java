package main;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args){
        try {
            Scanner scn = new Scanner(System.in);

            InetAddress ip = InetAddress.getByName(Config.INET_ADDRESS_NAME);
            Socket s = new Socket(ip, Config.PORT);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            while (true) {
                System.out.println("waiting next line");
                String line = br.readLine();
                System.out.println("sleeping");
                Thread.sleep(1_500);
                dos.write(("Client : " + line +"\n").getBytes());
                System.out.println(line);
//                if (line.equals("[[]]")){
//
//                }else{
//                    System.out.println("waiting" + line);
//                }
//                System.out.println(dis.readUTF());


                // If client sends exit,close this connection
                // and then break from the while loop
                if (line.equals("Exit")) {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }

                // printing date or time as requested by client
            }

            // closing resources
            scn.close();
            dis.close();
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
