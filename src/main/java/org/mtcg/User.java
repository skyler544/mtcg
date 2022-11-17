package org.mtcg;

import java.util.ArrayList;

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

    public void consultLoreMaster(Card card) {}

}
