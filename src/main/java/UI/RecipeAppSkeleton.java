package UI;

import edu.bsu.cs222.finalproject.JsonDataParser;
import edu.bsu.cs222.finalproject.RecipeGrabber;
import edu.bsu.cs222.finalproject.UserStorage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RecipeAppSkeleton extends Application {

    private UserStorage storage;

    @Override
    public void start(Stage stage) {

        storage = new UserStorage();

        LoginScreen loginScreen = new LoginScreen(storage);

        VBox loginLayout = loginScreen.getView(stage, () -> showRecipeFinder(stage));

        Scene loginScene = new Scene(loginLayout, 300, 200);

        stage.setScene(loginScene);
        stage.setTitle("Login");
        stage.show();
    }

    private void showRecipeFinder(Stage stage) {

        Label title = new Label("Recipe Finder");

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            storage.logout();
            LoginScreen loginScreen = new LoginScreen(storage);
            VBox loginLayout = loginScreen.getView(stage, () -> showRecipeFinder(stage));
            stage.setScene(new Scene(loginLayout, 300, 200));
        });

        TextField searchField = new TextField();
        searchField.setPromptText("Search for an ingredient");

        Button searchButton = new Button("Search");

        Button saveButton = new Button("Save Recipe");
        saveButton.setDisable(true);

        Button viewSavedButton = new Button("View Saved Recipes");

        ListView<String> resultsList = new ListView<>();

        // SEARCH HANDLER
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

                    Platform.runLater(() -> {
                        if (meals.length == 0) {
                            resultsList.getItems().setAll("No recipes found.");
                        } else {
                            resultsList.getItems().setAll(meals);
                        }
                    });

                } catch (Exception ex) {
                    Platform.runLater(() ->
                            resultsList.getItems().setAll("Error fetching recipes.")
                    );
                }
            }).start();
        });

        // CLICK HANDLER — FIXED + FORMATTED
        resultsList.setOnMouseClicked(event -> {
            String selected = resultsList.getSelectionModel().getSelectedItem();
            if (selected == null || !selected.contains("(ID:")) return;

            int idStart = selected.lastIndexOf("(ID:") + 4;
            int idEnd = selected.indexOf(")", idStart);

            if (idStart < 4 || idEnd == -1) return;

            String id = selected.substring(idStart, idEnd).trim();
            String mealName = selected.substring(0, selected.lastIndexOf(" (ID:"));

            resultsList.getItems().setAll("Loading recipe details...");

            new Thread(() -> {
                try {
                    RecipeGrabber grabber = new RecipeGrabber();
                    String json = grabber.fetchRecipeById(id);

                    JsonDataParser parser = new JsonDataParser();
                    String details = parser.parseMealDetails(json);

                    Platform.runLater(() -> {
                        resultsList.getItems().setAll(details.split("\n"));

                        saveButton.setDisable(false);
                        saveButton.setOnAction(ev -> {
                            storage.saveRecipe(mealName + "|" + id);
                        });
                    });

                } catch (Exception ex) {
                    Platform.runLater(() ->
                            resultsList.getItems().setAll("Error loading recipe details.")
                    );
                }
            }).start();
        });

        viewSavedButton.setOnAction(e -> {
            SavedRecipesScreen screen = new SavedRecipesScreen(storage);
            stage.setScene(screen.getScene(stage, () -> showRecipeFinder(stage)));
        });

        VBox layout = new VBox(
                10,
                title,
                logoutButton,
                searchField,
                searchButton,
                saveButton,
                viewSavedButton,
                resultsList
        );

        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 450, 600);

        stage.setScene(scene);
        stage.setTitle("Recipe Finder");
    }

    public static void main(String[] args) {
        launch();
    }
}
