package edu.bsu.cs222.finalproject;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final String username;
    private final String password;
    private final List<String> savedRecipes = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getSavedRecipes() {
        return new ArrayList<>(savedRecipes);
    }

    public String getSavedRecipesAsString() {
        return String.join(",", savedRecipes);
    }

    public void addRecipe(String recipeLine) {
        savedRecipes.add(recipeLine);
    }

    public void removeRecipe(String recipeLine) {
        savedRecipes.remove(recipeLine);
    }
}
