package org.example.customization;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReaderClient extends Thread {
    private static BufferedReader in;
    private final Client client;
    private final Thread writerClient;
    private final Socket socket;

    public ReaderClient(Socket socket, Client client, Thread writerClient) {
        this.socket = socket;
        this.writerClient = writerClient;
        this.client = client;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (writerClient.isAlive()) {
            try {
                String response = in.readLine();
                client.saveMessage(response);
                System.out.println("\n" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
