package UI;

import com.github.openjson.JSONArray;
import com.github.openjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class RecipeService {

    private String[] allergies = new String[0];

    public void setAllergies(String[] allergies) {
        this.allergies = allergies;
    }

    public String[] getAllergies() {
        return allergies;
    }

    private final Map<String, String> mealDetailsMap = new HashMap<>();

    public RecipeService() {}

    // MULTI-WORD SAFE SPLITTING
    public String[] parseIngredients(String input) {
        if (input == null || input.isBlank()) return new String[0];

        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toArray(String[]::new);
    }

    public String[] parseAllergies(String input) {
        if (input == null || input.isBlank()) return new String[0];

        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toArray(String[]::new);
    }

    // KEEP MULTI-WORD INGREDIENTS EXACT
    public String normalizeIngredient(String ing) {
        return ing.toLowerCase();
    }

    public String extractId(String recipeLine) {
        if (!recipeLine.contains("|")) return "";
        return recipeLine.substring(recipeLine.indexOf("|") + 1).trim();
    }

    private String fetchURL(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) sb.append(line);

            br.close();
            return sb.toString();

        } catch (Exception e) {
            return null;
        }
    }

    public String[] searchByIngredient(String ingredient) {
        ingredient = ingredient.replace(" ", "_"); // TheMealDB uses underscores

        String json = fetchURL(
                "https://www.themealdb.com/api/json/v1/1/filter.php?i=" + ingredient
        );

        if (json == null) return new String[0];

        JSONObject obj = new JSONObject(json);
        JSONArray meals = obj.optJSONArray("meals");

        if (meals == null) return new String[0];

        List<String> results = new ArrayList<>();

        for (int i = 0; i < meals.length(); i++) {
            JSONObject meal = meals.getJSONObject(i);

            String id = meal.getString("idMeal");
            String name = meal.getString("strMeal");

            results.add(name + " | " + id);
        }

        return results.toArray(new String[0]);
    }

    // NEW: Check if recipe contains allergen ANYWHERE in ingredients or instructions
    public boolean recipeContainsAllergen(String id, String[] allergies) {
        if (!mealDetailsMap.containsKey(id)) {
            loadMealDetails(id);
        }

        String details = mealDetailsMap.get(id).toLowerCase();

        for (String allergy : allergies) {
            if (allergy == null || allergy.isBlank()) continue;

            if (details.contains(allergy.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public String getMealDetails(String id, String[] allergies) {

        if (!mealDetailsMap.containsKey(id)) loadMealDetails(id);

        String details = mealDetailsMap.get(id);
        if (details == null) return "No details available.";

        for (String allergy : allergies) {
            if (allergy == null || allergy.isBlank()) continue;

            String lower = allergy.toLowerCase();
            String lowerDetails = details.toLowerCase();

            int index = lowerDetails.indexOf(lower);
            if (index != -1) {
                details =
                        details.substring(0, index)
                                + "[RED]"
                                + details.substring(index, index + allergy.length())
                                + "[/RED]"
                                + details.substring(index + allergy.length());
            }
        }

        return details;
    }

    private void loadMealDetails(String id) {
        String json = fetchURL(
                "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + id
        );

        if (json == null) return;

        JSONObject obj = new JSONObject(json);
        JSONArray meals = obj.optJSONArray("meals");

        if (meals == null) return;

        JSONObject meal = meals.getJSONObject(0);

        StringBuilder sb = new StringBuilder();

        sb.append("Name: ").append(meal.getString("strMeal")).append("\n\n");
        sb.append("Category: ").append(meal.optString("strCategory", "N/A")).append("\n");
        sb.append("Area: ").append(meal.optString("strArea", "N/A")).append("\n\n");

        sb.append("Ingredients:\n");

        for (int i = 1; i <= 20; i++) {
            String ing = meal.optString("strIngredient" + i, "");
            String measure = meal.optString("strMeasure" + i, "");

            if (ing != null && !ing.isBlank()) {
                sb.append("- ").append(ing).append(" (").append(measure).append(")\n");
            }
        }

        sb.append("\nInstructions:\n");
        sb.append(meal.optString("strInstructions", "No instructions available."));

        mealDetailsMap.put(id, sb.toString());
    }
}
