package edu.bsu.cs222.finalproject;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;

import java.io.IOException;


public class JsonDataParser {
    public String parse(String jsonData) throws IOException {

        JSONArray result = JsonPath.read(jsonData, "$..strMeal");
        String newResult = result.toString();
        newResult = newResult.replace("[", "");
        newResult = newResult.replace("]", "");
        return newResult;
    }
}