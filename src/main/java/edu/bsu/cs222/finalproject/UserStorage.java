package edu.bsu.cs222.finalproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserStorage {
    private ArrayList<User> users = new ArrayList<>();
    private User activeUser;

    public boolean addUser(String username, String password) {
        for (User user: users) {
            if (username.equals(user.getUsername())) {
                return false;
            }
        }

        User user = new User(username, password);

        users.add(user);
        return true;
    }

    public boolean login(String username, String password) {
        for (User user: users) {
            if (!username.equals(user.getUsername())) {
                return false;
            }
            else {
                activeUser = user;
            }
        }

        if (password.equals(activeUser.getPassword())) {
            return true;
        }

        else {
            return false;
        }
    }

    public boolean userExists(String username) {
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}