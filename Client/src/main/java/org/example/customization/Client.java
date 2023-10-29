package org.example.customization;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {
    private final String host;
    private final int port;
    private String userName;
    private static final String DIR = "fileClient.log";

    public Client() {
        try {
            Scanner scanner = new Scanner(new File("settingsClient.txt"));
            this.port = scanner.nextInt();
            this.host = scanner.next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void logIn() {
        try {
            Socket clientSocket = new Socket(host, port);
            System.out.println("Welcome to the game!");

            Thread thread = new WriterClient(clientSocket, this);
            thread.start();
            new ReaderClient(clientSocket, this, thread).start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveMessage(String message) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(DIR, true))) {
            bufferedWriter.write("[" + getCurrentTimeStamp() + "] " + message + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date now = new Date();
        return sdfDate.format(now);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

}
