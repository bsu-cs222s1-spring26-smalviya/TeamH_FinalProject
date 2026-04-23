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

                results[i] = name + " | " + id;
            }

            return results;

        } catch (Exception e) {
            return new String[0];
        }
    }

    public String parseMealDetails(String json) {
        try {
            JSONObject root = new JSONObject(json);

            if (!root.has("meals") || root.isNull("meals")) {
                return "No details available.";
            }

            JSONObject meal = root.getJSONArray("meals").getJSONObject(0);

            StringBuilder sb = new StringBuilder();

            sb.append("Name: ").append(meal.optString("strMeal")).append("\n");
            sb.append("Category: ").append(meal.optString("strCategory")).append("\n");
            sb.append("Area: ").append(meal.optString("strArea")).append("\n\n");

            sb.append("Instructions:\n")
                    .append(meal.optString("strInstructions"))
                    .append("\n\nIngredients:\n");

            for (int i = 1; i <= 20; i++) {
                String ing = meal.optString("strIngredient" + i, "").trim();
                String meas = meal.optString("strMeasure" + i, "").trim();

                if (!ing.isEmpty()) {
                    sb.append("- ").append(ing);
                    if (!meas.isEmpty()) sb.append(" (").append(meas).append(")");
                    sb.append("\n");
                }
            }

            return sb.toString();

        } catch (Exception e) {
            return "Error loading recipe details.";
        }
    }

    public String highlightAllergens(String text, String[] allergies) {
        if (text == null || allergies == null) return text;

        String result = text;

        for (String allergy : allergies) {

            // FIX: skip empty or blank allergens
            if (allergy == null || allergy.isBlank()) continue;

            String lower = allergy.toLowerCase();

            try {
                result = result.replaceAll("(?i)" + lower, "[RED]" + allergy + "[/RED]");
            } catch (Exception e) {
                // ignore bad regex
            }
        }

        return result;
    }
}