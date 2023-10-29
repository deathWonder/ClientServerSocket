package org.example.customization;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Server {
    private final int port;
    private final List<ClientHandler> clients = new LinkedList<>();
    private static final String DIR = "fileServer.log";

    public Server() {
        try {
            Scanner scanner = new Scanner(new File("settingsServer.txt"));
            port = scanner.nextInt();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started...");
            Socket clientSocket;
            try {
                while (true) {
                    clientSocket = serverSocket.accept();
                    System.out.println("New connection. Port: " + clientSocket.getPort());
                    ClientHandler client = new ClientHandler(clientSocket, this);
                    clients.add(client);
                    new Thread(client).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    serverSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToAllClients(String message, ClientHandler clientHandler) {
        for (ClientHandler client : clients) {
            if (client != clientHandler) {
                client.sendMessage(message);
            } else {
                saveMessage(message);
            }
        }
    }

    private void saveMessage(String message) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(DIR, true))) {
            bufferedWriter.write("[" + getCurrentTimeStamp() + "]" + message + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date now = new Date();
        return sdfDate.format(now);
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public int getPort() {
        return port;
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
