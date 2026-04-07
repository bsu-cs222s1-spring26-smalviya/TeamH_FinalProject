package UI;

import edu.bsu.cs222.finalproject.JsonDataParser;
import edu.bsu.cs222.finalproject.RecipeGrabber;
import edu.bsu.cs222.finalproject.UserStorage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RecipeAppSkeleton extends Application {

    private UserStorage storage;

    @Override
    public void start(Stage stage) {

        storage = new UserStorage(); // Load users from file

        // Show login screen first
        LoginScreen loginScreen = new LoginScreen(storage);

        VBox loginLayout = loginScreen.getView(stage, () -> {
            showRecipeFinder(stage); // Switch to recipe finder after login
        });

        Scene loginScene = new Scene(loginLayout, 300, 200);

        stage.setScene(loginScene);
        stage.setTitle("Login");
        stage.show();
    }

    // Updated Recipe Finder with Save + View Saved Recipes
    private void showRecipeFinder(Stage stage) {

        Label title = new Label("Recipe Finder");

        TextField searchField = new TextField();
        searchField.setPromptText("Search for an ingredient");

        Button searchButton = new Button("Search");

        // NEW BUTTONS
        Button saveButton = new Button("Save Recipe");
        saveButton.setDisable(true);

        Button viewSavedButton = new Button("View Saved Recipes");

        ListView<String> resultsList = new ListView<>();

        // SEARCH LOGIC
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

        // CLICK RECIPE → LOAD DETAILS + ENABLE SAVE BUTTON
        resultsList.setOnMouseClicked(event -> {
            String selected = resultsList.getSelectionModel().getSelectedItem();
            if (selected == null || !selected.contains("ID:")) {
                return;
            }

            String id = selected.substring(selected.indexOf("ID:") + 4, selected.indexOf(")"));
            String mealName = selected.substring(0, selected.indexOf(" (ID:"));

            resultsList.getItems().setAll("Loading recipe...");

            new Thread(() -> {
                try {
                    RecipeGrabber grabber = new RecipeGrabber();
                    String json = grabber.fetchRecipeById(id);

                    JsonDataParser parser = new JsonDataParser();
                    String details = parser.parseMealDetails(json);

                    javafx.application.Platform.runLater(() -> {
                        resultsList.getItems().setAll(details);

                        // ENABLE SAVE BUTTON
                        saveButton.setDisable(false);

                        // SAVE RECIPE FOR ACTIVE USER
                        saveButton.setOnAction(ev -> {
                            storage.saveRecipe(mealName);
                        });
                    });

                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() ->
                            resultsList.getItems().setAll("Error loading recipe details.")
                    );
                }
            }).start();
        });

        // NEW: VIEW SAVED RECIPES BUTTON
        viewSavedButton.setOnAction(e -> {
            SavedRecipesScreen screen = new SavedRecipesScreen(storage);
            stage.setScene(screen.getScene(stage, () -> showRecipeFinder(stage)));
        });

        VBox layout = new VBox(10, title, searchField, searchButton, saveButton, viewSavedButton, resultsList);

        Scene scene = new Scene(layout, 400, 550);

        stage.setScene(scene);
        stage.setTitle("Recipe Finder");
    }

    public static void main(String[] args) {
        launch();
    }
}
