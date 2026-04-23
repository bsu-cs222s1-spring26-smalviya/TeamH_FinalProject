package edu.bsu.cs222.finalproject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {

    private final File file = new File("users.txt");
    private final List<User> users = new ArrayList<>();
    private User activeUser;

    public UserStorage() {
        loadUsers();
    }

    public boolean createAccount(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }

        User newUser = new User(username, password);
        users.add(newUser);
        saveUsers();
        return true;
    }

    public boolean login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                activeUser = u;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        activeUser = null;
    }

    public boolean isLoggedIn() {
        return activeUser != null;
    }

    public User getActiveUser() {
        return activeUser;
    }

    public List<String> getSavedRecipes() {
        if (activeUser == null) return new ArrayList<>();
        return activeUser.getSavedRecipes();
    }

    public void addRecipe(String recipeLine) {
        if (activeUser == null) return;
        activeUser.addRecipe(recipeLine);
        saveUsers();
    }

    public void removeRecipe(String recipeLine) {
        if (activeUser == null) return;
        activeUser.removeRecipe(recipeLine);
        saveUsers();
    }

    private void loadUsers() {
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");

                if (parts.length < 2) {
                    System.out.println("Skipping invalid user line: " + line);
                    continue;
                }

                String username = parts[0];
                String password = parts[1];

                User user = new User(username, password);

                if (parts.length > 2) {
                    String[] recipes = parts[2].split(",");
                    for (String r : recipes) {
                        if (!r.isBlank()) {
                            user.addRecipe(r.trim());
                        }
                    }
                }

                users.add(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (User u : users) {
                writer.println(
                        u.getUsername() + ";" +
                                u.getPassword() + ";" +
                                u.getSavedRecipesAsString()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
