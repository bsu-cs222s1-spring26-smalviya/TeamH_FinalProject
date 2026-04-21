package edu.bsu.cs222.finalproject;

import com.github.openjson.JSONArray;
import com.github.openjson.JSONObject;

public class JsonDataParser {

    public String[] parseMealNamesAndIds(String json) {
        try {
            JSONObject root = new JSONObject(json);

            if (!root.has("meals") || root.isNull("meals")) {
                return new String[0];
            }

            JSONArray meals = root.getJSONArray("meals");
            String[] results = new String[meals.length()];

            for (int i = 0; i < meals.length(); i++) {
                JSONObject meal = meals.getJSONObject(i);

                String name = meal.optString("strMeal", "Unknown Meal");
                String id = meal.optString("idMeal", "UNKNOWN");

                results[i] = name + " (ID: " + id + ")";
            }

            return results;

        } catch (Exception e) {
            System.out.println("Error parsing meal names: " + e.getMessage());
            return new String[0];
        }
    }

    public String parseMealDetails(String json) {
        try {
            JSONObject root = new JSONObject(json);

            if (!root.has("meals") || root.isNull("meals")) {
                return "No details available.";
            }

            JSONArray meals = root.getJSONArray("meals");
            JSONObject meal = meals.getJSONObject(0);

            StringBuilder sb = new StringBuilder();

            String name = meal.optString("strMeal", "Unknown Meal");
            String category = meal.optString("strCategory", "Unknown Category");
            String area = meal.optString("strArea", "Unknown Area");
            String instructions = meal.optString("strInstructions", "No instructions available.");

            sb.append("Name: ").append(name).append("\n");
            sb.append("Category: ").append(category).append("\n");
            sb.append("Area: ").append(area).append("\n\n");
            sb.append("Instructions:\n").append(instructions).append("\n\n");
            sb.append("Ingredients:\n");

            for (int i = 1; i <= 20; i++) {
                String ingredient = meal.optString("strIngredient" + i, "").trim();
                String measure = meal.optString("strMeasure" + i, "").trim();

                if (!ingredient.isEmpty()) {
                    sb.append("- ").append(ingredient);
                    if (!measure.isEmpty()) sb.append(" (").append(measure).append(")");
                    sb.append("\n");
                }
            }

            return sb.toString();

        } catch (Exception e) {
            System.out.println("Error parsing meal details: " + e.getMessage());
            return "Error loading recipe details.";
        }
    }

    public String highlightAllergensInText(String text, String[] allergies) {
        if (text == null || allergies == null) return text;

        String result = text;

        try {
            for (String allergy : allergies) {
                if (allergy == null || allergy.isBlank()) continue;

                String lower = allergy.toLowerCase();
                result = result.replaceAll("(?i)" + lower, "[RED]" + allergy + "[/RED]");
            }
        } catch (Exception e) {
            System.out.println("Error highlighting allergens: " + e.getMessage());
        }

        return result;
    }
}
