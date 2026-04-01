package edu.bsu.cs222.finalproject;

import com.jayway.jsonpath.JsonPath;
import java.util.List;

public class JsonDataParser {

    // NEW: Ingredient search now returns "Name (ID: ####)"
    public String[] parseMealNamesAndIds(String jsonData) {
        try {
            List<String> names = JsonPath.read(jsonData, "$.meals[*].strMeal");
            List<String> ids = JsonPath.read(jsonData, "$.meals[*].idMeal");

            String[] result = new String[names.size()];
            for (int i = 0; i < names.size(); i++) {
                result[i] = names.get(i) + " (ID: " + ids.get(i) + ")"; // NEW
            }
            return result;

        } catch (Exception e) {
            return new String[0];
        }
    }

    // NEW: Full recipe detail parsing
    public String parseMealDetails(String jsonData) {
        try {
            String name = JsonPath.read(jsonData, "$.meals[0].strMeal");
            String instructions = JsonPath.read(jsonData, "$.meals[0].strInstructions");

            return name + "\n\nInstructions:\n" + instructions; // NEW
        } catch (Exception e) {
            return "Error parsing recipe details.";
        }
    }
}

