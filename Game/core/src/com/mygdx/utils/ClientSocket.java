package com.mygdx.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientSocket {
    public static void main(String[] args) {
        try {
            // Reemplaza con la dirección IP asignada al Raspberry Pi Pico
            String serverAddress = "192.168.0.15";
            int serverPort = 8080;

            // Establecer conexión al servidor
            Socket socket = new Socket(serverAddress, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Leer datos continuamente desde el Raspberry Pi Pico
            while (true) {
                // Leer la respuesta del Raspberry Pi Pico
                String response = in.readLine();

                if (response == null) {
                    // Si la respuesta es nula, la conexión se cerró
                    System.out.println("Conexión cerrada por el servidor");
                    break;
                }

                System.out.println("Respuesta del Raspberry Pi Pico: " + response);
            }

            // Cerrar la conexión
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


