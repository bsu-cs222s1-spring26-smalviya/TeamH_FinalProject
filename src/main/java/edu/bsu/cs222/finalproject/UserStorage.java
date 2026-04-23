package edu.bsu.cs222.finalproject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {

    private final File file = new File("users.txt");
    private String currentUser = null;

    public UserStorage() {
        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean login(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    if (parts[0].trim().equals(username) &&
                            parts[1].trim().equals(password)) {
                        currentUser = username;
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean createAccount(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 1) {
                    if (parts[0].trim().equals(username)) {
                        return false;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(username + ";" + password + ";" + System.lineSeparator());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public List<String> getSavedRecipes() {
        List<String> list = new ArrayList<>();

        if (currentUser == null) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length >= 3 && parts[0].trim().equals(currentUser)) {
                    if (parts[2].trim().isEmpty()) return list;

                    String[] recipes = parts[2].split(",");
                    for (String r : recipes) {
                        if (!r.isBlank()) list.add(r.trim());
                    }
                    return list;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean addRecipe(String recipeLine) {
        if (currentUser == null) return false;

        List<String> saved = getSavedRecipes();
        if (saved.contains(recipeLine)) return false;

        saved.add(recipeLine);
        writeUpdatedUserLine(saved);

        return true;
    }

    public void removeRecipe(String recipeLine) {
        if (currentUser == null) return;

        List<String> saved = getSavedRecipes();
        saved.remove(recipeLine);

        writeUpdatedUserLine(saved);
    }

    private void writeUpdatedUserLine(List<String> recipes) {
        List<String> allLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length >= 2 && parts[0].trim().equals(currentUser)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(parts[0]).append(";").append(parts[1]).append(";");

                    for (String r : recipes) {
                        sb.append(r).append(",");
                    }

                    allLines.add(sb.toString());
                } else {
                    allLines.add(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter pw = new PrintWriter(file)) {
            for (String s : allLines) pw.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
