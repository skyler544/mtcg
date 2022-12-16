package org.mtcg;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private static ServerSocket _listener = null;

    public static void main(String[] args) {
        System.out.println("start server");

        try {
            _listener = new ServerSocket(10001, 5);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Server()));

        try {
            while (true) {
                Socket s = _listener.accept();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

                System.out.println("srv: sending welcome message");
                writer.write("Welcome to MTCG!");
                writer.newLine();
                writer.flush();

                boolean authenticated = false;

                while (!authenticated) {
                    writer.write("Please authenticate or register: [a/r]");
                    writer.newLine();
                    writer.flush();

                    String message = reader.readLine();

                    switch (message) {
                    case "a":
                        System.out.println("Authenticated.");
                        authenticated = true;
                        break;
                    case "r":
                        System.out.println("Registered.");
                        authenticated = true;
                        break;
                    default:
                        break;
                    }
                }

                writer.write("Enter a command: ");
                writer.newLine();
                writer.flush();

                String message;
                do {
                    message = reader.readLine();
                    CommandDispatcher.dispatch(message);
                } while (!"quit".equals(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            _listener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        _listener = null;
        System.out.println("close server");
    }
}
