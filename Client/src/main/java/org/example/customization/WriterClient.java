package org.example.customization;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriterClient extends Thread {
    private final Client client;
    private static PrintWriter out;

    public WriterClient(Socket socket, Client client) {
        this.client = client;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username <-> ");
        String userName = scanner.nextLine();
        client.setUserName(userName);
        try {
            out.println(userName);

            String message;

            while (true) {
                message = scanner.nextLine();
                if (message.equals("/exit")) {
                    out.println(message);
                    break;
                }
                client.saveMessage(client.getUserName() + "<->" + message);
                out.println(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
