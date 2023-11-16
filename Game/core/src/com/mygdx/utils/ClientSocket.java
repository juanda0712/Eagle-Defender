package com.mygdx.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientSocket {
    public static void main(String[] args) {
        try {
            String serverAddress = "192.168.0.15";
            int serverPort = 8080;

            Socket socket = new Socket(serverAddress, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String response = in.readLine();

                if (response == null) {
                    System.out.println("Conexión cerrada por el servidor");
                    break;
                }

                System.out.println(response);
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}