package com.playground;


import com.Config;
import com.util.Core;
import com.util.StringVector;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        Scanner scn = new Scanner(System.in);
        int counter = 0;

        InetAddress ip = InetAddress.getByName(Config.INET_ADDRESS_NAME);
        Socket s = new Socket(ip, Config.PORT);

        ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

        while (true) {
            System.out.println("waiting next line");
            counter++;
            Double[][] data = (Double[][]) objectInputStream.readObject();
            System.out.println(Arrays.deepToString(data));
            System.out.println("Counter = " + counter);

//                if (line.equals("[[]]")){
//
//                }else{
//                    System.out.println("waiting" + line);
//                }
//                System.out.println(dis.readUTF());


            // If client sends exit,close this connection
            // and then break from the while loop

            // printing date or time as requested by client
//            }
        }

            // closing resources

//            try {
//                dis.close();
//                dos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
}
