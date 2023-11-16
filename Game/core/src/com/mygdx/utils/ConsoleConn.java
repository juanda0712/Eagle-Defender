package com.mygdx.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConsoleConn {
    private String status;

    public ConsoleConn() {
        status = "No actualizado";
        startConnectionThread();
    }

    private void startConnectionThread() {
        Thread connectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                connection();
            }
        });
        connectionThread.start();
    }

    private void connection() {
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
                this.status = response;
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStatus() {
        return this.status;
    }
}