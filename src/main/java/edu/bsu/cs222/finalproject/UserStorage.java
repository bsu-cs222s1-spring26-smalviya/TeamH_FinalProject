package edu.bsu.cs222.finalproject;

import java.io.*;
import java.util.ArrayList;

public class UserStorage {

    private ArrayList<User> users = new ArrayList<>();
    private User activeUser;

    // File where users are stored
    private final File file = new File("users.txt");

    public UserStorage() {
        loadUsers(); // Load users on startup
    }

    // SAFELY load users from file
    private void loadUsers() {
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {

                // Skip empty or whitespace-only lines
                if (line.trim().isEmpty()) continue;

                // Split on ANY whitespace (fixes multiple spaces)
                String[] parts = line.trim().split("\\s+");

                // Must have at least username + password
                if (parts.length < 2) {
                    System.out.println("Skipping invalid user line: " + line);
                    continue;
                }

                String username = parts[0];
                String password = parts[1];

                User user = new User(username, password);

                // Load saved recipes if present
                for (int i = 2; i < parts.length; i++) {
                    user.addRecipe(parts[i]);
                }

                users.add(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save all users back to file
    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (User user : users) {
                writer.write(user.getUsername() + " " + user.getPassword());

                // Write saved recipes
                for (String recipe : user.getSavedRecipes()) {
                    writer.write(" " + recipe);
                }

                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create a new account
    public boolean createAccount(String username, String password) {
        if (userExists(username)) return false;

        User user = new User(username, password);
        users.add(user);
        saveUsers();
        return true;
    }

    // Login and set active user
    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) &&
                    user.getPassword().equals(password)) {

                activeUser = user;
                return true;
            }
        }
        return false;
    }

    // Save a recipe for the logged-in user
    public void saveRecipe(String recipeName) {
        if (activeUser == null) return;

        activeUser.addRecipe(recipeName);
        saveUsers();
    }

    // Check if username already exists
    public boolean userExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    // Get the currently logged-in user
    public User getActiveUser() {
        return activeUser;
    }
}
