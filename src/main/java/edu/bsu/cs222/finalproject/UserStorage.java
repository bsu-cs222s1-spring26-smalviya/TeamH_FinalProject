package edu.bsu.cs222.finalproject;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserStorage {

    private ArrayList<User> users = new ArrayList<>();
    private User activeUser;

    private final File file = new File("users.txt");

    public UserStorage() {
        loadUsers();
    }

    private void loadUsers() {
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                // Keep quoted strings together
                ArrayList<String> parts = new ArrayList<>();
                Matcher m = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(line);

                while (m.find()) {
                    if (m.group(1) != null) parts.add(m.group(1));
                    else parts.add(m.group(2));
                }

                if (parts.size() < 2) continue;

                String username = parts.get(0);
                String password = parts.get(1);

                User user = new User(username, password);

                // Load saved recipes
                if (parts.size() > 2) {
                    for (int i = 2; i < parts.size(); i++) {
                        String merged = parts.get(i).trim();
                        if (merged.isEmpty()) continue;

                        ArrayList<String> recipes = new ArrayList<>();
                        StringBuilder current = new StringBuilder();

                        int depth = 0;

                        for (int j = 0; j < merged.length(); j++) {
                            char c = merged.charAt(j);

                            if (c == '(') depth++;
                            if (c == ')') depth--;

                            if (c == ' ' && depth == 0 &&
                                    j + 1 < merged.length() &&
                                    Character.isUpperCase(merged.charAt(j + 1))) {

                                recipes.add(current.toString().trim());
                                current.setLength(0);
                            } else {
                                current.append(c);
                            }
                        }

                        if (current.length() > 0) {
                            recipes.add(current.toString().trim());
                        }

                        for (String recipe : recipes) {
                            if (recipe.contains("|")) {
                                user.addRecipe(recipe);
                            } else {
                                user.addRecipe(recipe + "|UNKNOWN");
                            }
                        }
                    }
                }

                users.add(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        saveUsers();
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (User user : users) {
                writer.write(user.getUsername() + " " + user.getPassword());

                for (String recipe : user.getSavedRecipes()) {
                    writer.write(" \"" + recipe + "\"");
                }

                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUsersFromOutside() {
        saveUsers();
    }

    public boolean createAccount(String username, String password) {
        if (userExists(username)) return false;

        User user = new User(username, password);
        users.add(user);
        saveUsers();
        return true;
    }

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

    public void logout() {
        activeUser = null;
    }

    public void saveRecipe(String recipeName) {
        if (activeUser == null) return;

        activeUser.addRecipe(recipeName);
        saveUsers();
    }

    public boolean userExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public User getActiveUser() {
        return activeUser;
    }
}
