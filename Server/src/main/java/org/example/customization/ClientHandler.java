package org.example.customization;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Server server;
    private PrintWriter outMessage;
    private static int countClients = 0;

    public ClientHandler(Socket clientSocket, Server server) {
        countClients++;
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {

        try {
            outMessage = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader inMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String userName = inMessage.readLine();
            outMessage.println("You are welcome " + userName + "!");
            outMessage.println("Now you are " + countClients + "!");
            server.sendMessageToAllClients(userName + " joined us!", this);
            server.sendMessageToAllClients("Now we are " + countClients + "!", this);


            boolean work = true;
            do {
                String clientMessage = inMessage.readLine();
                if (clientMessage != null) {
                    if (clientMessage.equals("/exit")) {
                        outMessage.println("Bye-bye, " + userName + "!");
                        work = false;
                    }
                    if (!clientMessage.equals("/exit")) {
                        server.sendMessageToAllClients(userName + " <-> " + clientMessage, this);
                    }
                }
            }
            while (work);
            closeConnection(userName);
            System.out.println("Disconnect. Port: " + clientSocket.getPort());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendMessage(String message) {
        outMessage.println(message);
    }

    private void closeConnection(String userName) {
        countClients--;
        server.removeClient(this);
        server.sendMessageToAllClients(userName + " - " + "LEAVE GAME!!!", this);
    }

    public static int getCountClients() {
        return countClients;
    }
}
