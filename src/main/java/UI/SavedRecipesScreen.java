package UI;

import edu.bsu.cs222.finalproject.User;
import edu.bsu.cs222.finalproject.UserStorage;
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
        list.getItems().addAll(user.getSavedRecipes());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> onBack.run());

        VBox layout = new VBox(10, title, list, backButton);

        return new Scene(layout, 300, 400);
    }
}
