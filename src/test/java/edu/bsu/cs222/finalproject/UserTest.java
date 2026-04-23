package edu.bsu.cs222.finalproject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void addRecipe() {
        User user = new User("Peng", "1234");
        user.addRecipe("Hotdog");
        System.out.println(user.getSavedRecipes());
        assertTrue(user.getSavedRecipes().contains("Hotdog"));
    }

    @Test
    void removeRecipe() {
        User user = new User("Peng", "1234");
        user.addRecipe("Hotdog");
        user.removeRecipe("Hotdog");
        assertFalse(user.getSavedRecipes().contains("Hotdog"));
    }
}


