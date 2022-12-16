package org.mtcg;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class User {
    private String username;
    private String password;
    private ArrayList<Card> stack;

    public User(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public ArrayList<Card> getStack() {
        return stack;
    }

    public void setStack(ArrayList<Card> stack) {
        this.stack = stack;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void consultLoreMaster(Card card) {
    }

    public static void main(String[] args) {
        System.out.println("start client");

        try (Socket socket = new Socket("localhost", 10001);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            System.out.println("cli: " + reader.readLine());
            System.out.println("cli: " + reader.readLine());
            String input = null;
            System.out.print("cli: ");
            while (!"quit".equals(input = consoleReader.readLine())) {
                writer.write(input);
                writer.newLine();
                writer.flush();
                System.out.print("cli: " + reader.readLine());
            }
            writer.write("quit");
            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("close client");

    }

}
