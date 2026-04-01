package edu.bsu.cs222.finalproject;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private ArrayList<String> savedRecipes;

    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public void addRecipe(String ID) {
        savedRecipes.add(ID);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getSavedRecipes() {
        return savedRecipes;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSavedRecipes(ArrayList<String> savedRecipes) {
        this.savedRecipes = savedRecipes;
    }
}
