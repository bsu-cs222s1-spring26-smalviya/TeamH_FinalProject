package edu.bsu.cs222.finalproject;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;

public class JsonDataParser {

    public String[] parseMeals(String jsonData) {
        JSONArray result = JsonPath.read(jsonData, "$.meals[*].strMeal");
        return result.toArray(new String[0]);
    }
}