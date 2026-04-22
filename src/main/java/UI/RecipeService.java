package UI;

import edu.bsu.cs222.finalproject.JsonDataParser;
import edu.bsu.cs222.finalproject.RecipeGrabber;

public class RecipeService {

    private final RecipeGrabber grabber = new RecipeGrabber();
    private final JsonDataParser parser = new JsonDataParser();

    // Search by ingredient
    public String[] searchByIngredient(String ingredient) {
        String json = grabber.getMealsByIngredient(ingredient);
        return parser.parseMealNamesAndIds(json);
    }

    // Load recipe details + highlight allergens
    public String getMealDetails(String id, String[] allergies) {
        String json = grabber.getMealDetails(id);
        String details = parser.parseMealDetails(json);
        return parser.highlightAllergens(details, allergies);
    }

    // Extract ID from "Meal Name | 12345"
    public String extractId(String recipeLine) {
        if (recipeLine == null || !recipeLine.contains("|")) return "UNKNOWN";
        return recipeLine.substring(recipeLine.lastIndexOf("|") + 1).trim();
    }

    // Convert allergy input into array
    public String[] parseAllergies(String raw) {
        if (raw == null || raw.isBlank()) return new String[0];
        return raw.toLowerCase().split(",");
    }

    // ⭐ NEW: Highlight allergens in ANY text (used for search results too)
    public String highlightAllergens(String text, String[] allergies) {
        return parser.highlightAllergens(text, allergies);
    }
}
