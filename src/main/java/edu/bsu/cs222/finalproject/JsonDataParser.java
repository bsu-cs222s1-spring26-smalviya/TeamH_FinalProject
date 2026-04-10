package edu.bsu.cs222.finalproject;

import com.github.openjson.JSONArray;
import com.github.openjson.JSONObject;

public class JsonDataParser {

    public String[] parseMealNamesAndIds(String json) {
        JSONObject obj = new JSONObject(json);

        if (obj.isNull("meals")) {
            return new String[0];
        }

        JSONArray arr = obj.getJSONArray("meals");
        String[] results = new String[arr.length()];

        for (int i = 0; i < arr.length(); i++) {
            JSONObject meal = arr.getJSONObject(i);
            String name = meal.getString("strMeal");
            String id = meal.getString("idMeal");
            results[i] = name + " (ID: " + id + ")";
        }

        return results;
    }

    public String parseMealDetails(String json) {
        JSONObject obj = new JSONObject(json);

        if (obj.isNull("meals")) {
            return "No details found.";
        }

        JSONObject meal = obj.getJSONArray("meals").getJSONObject(0);

        StringBuilder sb = new StringBuilder();

        sb.append("Name: ").append(meal.getString("strMeal")).append("\n\n");
        sb.append("Category: ").append(meal.optString("strCategory", "Unknown")).append("\n");
        sb.append("Area: ").append(meal.optString("strArea", "Unknown")).append("\n\n");
        sb.append("Instructions:\n").append(meal.getString("strInstructions")).append("\n\n");
        sb.append("Ingredients:\n");

        for (int i = 1; i <= 20; i++) {
            String ingredient = meal.optString("strIngredient" + i, "");
            String measure = meal.optString("strMeasure" + i, "");

            if (ingredient != null && !ingredient.isBlank()) {
                sb.append("- ").append(ingredient).append(" : ").append(measure).append("\n");
            }
        }

        return sb.toString();
    }
}
