package UI;

import edu.bsu.cs222.finalproject.User;
import edu.bsu.cs222.finalproject.UserStorage;
import edu.bsu.cs222.finalproject.JsonDataParser;
import edu.bsu.cs222.finalproject.RecipeGrabber;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SavedRecipesScreen {

    private final UserStorage storage;

    public SavedRecipesScreen(UserStorage storage) {
        this.storage = storage;
    }

    public Scene getScene(Stage stage, Runnable onBack) {

        User user = storage.getActiveUser();

        Label title = new Label("Saved Recipes for " + user.getUsername());

        ListView<String> list = new ListView<>();

        // Show only the recipe names (before the |)
        for (String entry : user.getSavedRecipes()) {
            String name = entry.split("\\|")[0];
            list.getItems().add(name);
        }

        // Click to load full recipe details
        list.setOnMouseClicked(e -> {
            String selectedName = list.getSelectionModel().getSelectedItem();
            if (selectedName == null) return;

            String full = user.getSavedRecipes().stream()
                    .filter(s -> s.startsWith(selectedName + "|"))
                    .findFirst().orElse(null);

            if (full == null) return;

            String[] parts = full.split("\\|");
            String id = parts.length > 1 ? parts[1] : "UNKNOWN";

            if (id.equals("UNKNOWN")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(selectedName);
                alert.setHeaderText(null);
                alert.setContentText("This recipe was saved before IDs were tracked, so details can't be reloaded.");
                alert.showAndWait();
                return;
            }

            new Thread(() -> {
                try {
                    RecipeGrabber grabber = new RecipeGrabber();
                    String json = grabber.fetchRecipeById(id);

                    JsonDataParser parser = new JsonDataParser();
                    String details = parser.parseMealDetails(json);

                    javafx.application.Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(selectedName);
                        alert.setHeaderText(null);
                        alert.setContentText(details);
                        alert.showAndWait();
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        Button deleteButton = new Button("Delete Selected Recipe");
        deleteButton.setOnAction(e -> {
            String selectedName = list.getSelectionModel().getSelectedItem();
            if (selectedName == null) return;

            String full = user.getSavedRecipes().stream()
                    .filter(s -> s.startsWith(selectedName + "|"))
                    .findFirst().orElse(null);

            if (full == null) return;

            user.getSavedRecipes().remove(full);
            storage.saveUsersFromOutside();
            list.getItems().remove(selectedName);
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> onBack.run());

        VBox layout = new VBox(10, title, list, deleteButton, backButton);

        return new Scene(layout, 300, 450);
    }
}
