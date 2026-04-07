package edu.bsu.cs222.finalproject;

import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class RecipeGrabber {

    public String fetchRecipesByIngredient(String ingredient) throws IOException {
        String encodedIngredient = URLEncoder.encode(ingredient, StandardCharsets.UTF_8);
        String url = "https://www.themealdb.com/api/json/v1/1/filter.php?i=" + encodedIngredient;

        URLConnection connection = URI.create(url).toURL().openConnection();

        // MAJOR CHANGE: added User-Agent header to avoid API blocking
        connection.setRequestProperty("User-Agent", "CS222-FinalProject/1.0");

        return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    // MAJOR FEATURE: fetch full recipe details by ID
    public String fetchRecipeById(String id) throws IOException {
        String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + id;

        URLConnection connection = URI.create(url).toURL().openConnection();
        connection.setRequestProperty("User-Agent", "CS222-FinalProject/1.0");

        return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}

