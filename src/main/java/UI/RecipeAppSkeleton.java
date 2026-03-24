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
        searchField.setPromptText("Search for an ingredient...");

        Button searchButton = new Button("Search");
        ListView<String> resultsList = new ListView<>();

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
                    String[] meals = parser.parseMeals(json);

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

        VBox layout = new VBox(10, title, searchField, searchButton, resultsList);
        Scene scene = new Scene(layout, 400, 500);

        stage.setScene(scene);
        stage.setTitle("Recipe Finder");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
