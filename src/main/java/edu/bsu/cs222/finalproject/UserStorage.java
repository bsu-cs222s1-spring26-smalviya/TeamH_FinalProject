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
        if (userExists(username)) return false;

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

    private boolean userExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public List<String> getSavedRecipes() {
        if (activeUser == null) return new ArrayList<>();
        return new ArrayList<>(activeUser.getSavedRecipes());
    }

    public void saveRecipe(String recipeLine) {
        if (activeUser == null) return;

        activeUser.getSavedRecipes().add(recipeLine);
        saveUsers();
    }

    public void deleteRecipe(String recipeLine) {
        if (activeUser == null) return;

        activeUser.getSavedRecipes().remove(recipeLine);
        saveUsers();
    }

    private void loadUsers() {
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String username = parts[0];
                String password = parts[1];

                User user = new User(username, password);

                if (parts.length > 2) {
                    String[] recipes = parts[2].split(",");
                    for (String r : recipes) {
                        if (!r.isBlank()) {
                            user.getSavedRecipes().add(r.trim());
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
                StringBuilder sb = new StringBuilder();
                sb.append(u.getUsername()).append(";")
                        .append(u.getPassword()).append(";");

                for (String recipe : u.getSavedRecipes()) {
                    sb.append(recipe).append(",");
                }

                writer.println(sb);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
