package edu.bsu.cs222.finalproject;

import java.util.HashMap;
import java.util.Map;

public class UserStorage {
    private Map<String, String> users;

    public UserStorage() {
        this.users = new HashMap<>();
    }

    public boolean addUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }

        users.put(username, password);
        return true;
    }

    public boolean login(String username, String password) {
        if (!users.containsKey(username)) {
            return false;
        }

        return users.get(username).equals(password);
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public Map<String, String> getAllUsers() {
        return users;
    }
}