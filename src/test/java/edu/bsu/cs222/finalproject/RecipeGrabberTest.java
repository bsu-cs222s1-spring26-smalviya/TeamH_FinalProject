package edu.bsu.cs222.finalproject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RecipeGrabberTest {

    @Test
    public void testFetchRecipesByIngredient() throws Exception {
        RecipeGrabber grabber = new RecipeGrabber();
        String json = grabber.fetchRecipesByIngredient("chicken");

        assertNotNull(json, "API returned null");
        assertTrue(json.contains("\"meals\""), "JSON missing 'meals' field");
    }

    @Test
    public void testFetchRecipeById() throws Exception {
        RecipeGrabber grabber = new RecipeGrabber();

        // Known valid ID from TheMealDB
        String json = grabber.fetchRecipeById("52772");

        assertNotNull(json, "API returned null");
        assertTrue(json.contains("\"meals\""), "JSON missing 'meals' field");
        assertTrue(json.contains("strMeal"), "JSON missing meal details");
    }
}
