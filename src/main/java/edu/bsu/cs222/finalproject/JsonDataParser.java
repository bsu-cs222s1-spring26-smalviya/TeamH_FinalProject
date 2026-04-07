package edu.bsu.cs222.finalproject;

import com.jayway.jsonpath.JsonPath;
import java.util.List;

public class JsonDataParser {

    // MAJOR FEATURE: now returns "Meal Name (ID: ####)"
    public String[] parseMealNamesAndIds(String jsonData) {
        try {
            List<String> names = JsonPath.read(jsonData, "$.meals[*].strMeal");
            List<String> ids = JsonPath.read(jsonData, "$.meals[*].idMeal");

            String[] result = new String[names.size()];
            for (int i = 0; i < names.size(); i++) {
                result[i] = names.get(i) + " (ID: " + ids.get(i) + ")";
            }
            return result;

        } catch (Exception e) {
            return new String[0];
        }
    }

    // MAJOR FEATURE: full recipe details (name + instructions)
    public String parseMealDetails(String jsonData) {
        try {
            String name = JsonPath.read(jsonData, "$.meals[0].strMeal");
            String instructions = JsonPath.read(jsonData, "$.meals[0].strInstructions");

            return name + "\n\nInstructions:\n" + instructions;

        } catch (Exception e) {
            return "Error parsing recipe details.";
        }
    }
}
