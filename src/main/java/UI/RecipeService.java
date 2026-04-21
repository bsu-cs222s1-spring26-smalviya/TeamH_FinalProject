package UI;

import edu.bsu.cs222.finalproject.JsonDataParser;
import edu.bsu.cs222.finalproject.RecipeGrabber;

import java.util.ArrayList;

public class RecipeService {

    private final RecipeGrabber grabber = new RecipeGrabber();
    private final JsonDataParser parser = new JsonDataParser();

    public ArrayList<String> searchRecipes(String ingredient, String allergyText) {
        try {
            String json = grabber.fetchRecipesByIngredient(ingredient);
            String[] meals = parser.parseMealNamesAndIds(json);

            String[] allergyList = parseAllergies(allergyText);
            ArrayList<String> finalList = new ArrayList<>();

            for (String meal : meals) {
                boolean unsafe = recipeContainsAllergen(meal, allergyList);
                finalList.add(unsafe ? "[RED]" + meal + "[/RED]" : meal);
            }

            return finalList;

        } catch (Exception e) {
            ArrayList<String> error = new ArrayList<>();
            error.add("Error fetching recipes.");
            return error;
        }
    }

    public String loadRecipeDetails(String id, String allergyText) {
        try {
            String json = grabber.fetchRecipeById(id);
            String details = parser.parseMealDetails(json);

            String[] allergyList = parseAllergies(allergyText);
            return parser.highlightAllergensInText(details, allergyList);

        } catch (Exception e) {
            return "Error loading recipe details.";
        }
    }

    public boolean recipeContainsAllergen(String meal, String[] allergyList) throws Exception {
        String lowerMeal = meal.toLowerCase();

        for (String allergy : allergyList) {
            if (!allergy.isEmpty() && lowerMeal.contains(allergy)) {
                return true;
            }
        }

        String id = extractId(meal);
        String detailsJson = grabber.fetchRecipeById(id);
        String details = parser.parseMealDetails(detailsJson).toLowerCase();

        for (String allergy : allergyList) {
            if (!allergy.isEmpty() && details.contains(allergy)) {
                return true;
            }
        }

        return false;
    }

    public String extractId(String meal) {
        int idStart = meal.lastIndexOf("(ID:") + 4;
        int idEnd = meal.indexOf(")", idStart);
        return meal.substring(idStart, idEnd).trim();
    }

    public String[] parseAllergies(String text) {
        String trimmed = text.trim().toLowerCase();
        return trimmed.isEmpty() ? new String[0] : trimmed.split(",");
    }
}
