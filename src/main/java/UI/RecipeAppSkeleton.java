package UI;

import edu.bsu.cs222.finalproject.JsonDataParser;
import edu.bsu.cs222.finalproject.RecipeGrabber;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RecipeAppSkeleton extends Application {

    @Override
    public void start(Stage stage) {
        Label title = new Label("Recipe Finder");

        TextField searchField = new TextField();
        searchField.setPromptText("Search for an ingredient");

        Button searchButton = new Button("Search");

        ListView<String> resultsList = new ListView<>();

        // Ingredient search
        searchButton.setOnAction(e -> {
            String ingredient = searchField.getText().trim();

            if (ingredient.isEmpty()) {
                resultsList.getItems().setAll("Please enter an ingredient.");
                return;
            }

            resultsList.getItems().setAll("Searching...");

            new Thread(() -> {
                try {
                    RecipeGrabber grabber = new RecipeGrabber();
                    String json = grabber.fetchRecipesByIngredient(ingredient);

                    JsonDataParser parser = new JsonDataParser();

                    // NEW: now using parseMealNamesAndIds()
                    String[] meals = parser.parseMealNamesAndIds(json);

                    javafx.application.Platform.runLater(() -> {
                        if (meals.length == 0) {
                            resultsList.getItems().setAll("No recipes found.");
                        } else {
                            resultsList.getItems().setAll(meals);
                        }
                    });

                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() ->
                            resultsList.getItems().setAll("Error fetching recipes.")
                    );
                }
            }).start();
        });

        // NEW: Clicking a meal loads full recipe details
        resultsList.setOnMouseClicked(event -> {
            String selected = resultsList.getSelectionModel().getSelectedItem();
            if (selected == null || !selected.contains("ID:")) {
                return;
            }

            // NEW: Extract ID from "(ID: ####)"
            String id = selected.substring(selected.indexOf("ID:") + 4, selected.indexOf(")"));

            resultsList.getItems().setAll("Loading recipe...");

            new Thread(() -> {
                try {
                    RecipeGrabber grabber = new RecipeGrabber();

                    // NEW: fetch full recipe by ID
                    String json = grabber.fetchRecipeById(id);

                    JsonDataParser parser = new JsonDataParser();

                    // NEW: parse full recipe details
                    String details = parser.parseMealDetails(json);

                    javafx.application.Platform.runLater(() ->
                            resultsList.getItems().setAll(details)
                    );

                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() ->
                            resultsList.getItems().setAll("Error loading recipe details.")
                    );
                }
            }).start();
        });

        VBox layout = new VBox(
                10,
                title,
                searchField,
                searchButton,
                resultsList
        );

        Scene scene = new Scene(layout, 400, 550);

        stage.setScene(scene);
        stage.setTitle("Recipe Finder");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


