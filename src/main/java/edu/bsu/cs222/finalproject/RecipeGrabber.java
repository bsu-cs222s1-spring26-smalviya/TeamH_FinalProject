package edu.bsu.cs222.finalproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecipeGrabber {

    private String fetch(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
            conn.setRequestProperty("Pragma", "no-cache");
            conn.setRequestProperty("Expires", "0");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            reader.close();
            conn.disconnect();
            return sb.toString();

        } catch (Exception e) {
            return "{}";
        }
    }

    public String getMealsByIngredient(String ingredient) {
        return fetch("https://www.themealdb.com/api/json/v1/1/filter.php?i=" + ingredient);
    }

    public String getMealDetails(String id) {
        return fetch("https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + id);
    }
}
