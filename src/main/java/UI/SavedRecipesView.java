package UI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class SavedRecipesView {

    private final VBox root = new VBox(10);
    private final ListView<String> savedList = new ListView<>();
    private final Button deleteButton = new Button("Delete Selected");
    private final Button viewButton = new Button("View Recipe");
    private final Button backButton = new Button("Back");

    public SavedRecipesView() {
        root.setPadding(new Insets(20));
        root.getChildren().addAll(savedList, deleteButton, viewButton, backButton);
    }

    public VBox getRoot() {
        return root;
    }

    public ListView<String> getSavedList() {
        return savedList;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getViewButton() {
        return viewButton;
    }

    public Button getBackButton() {
        return backButton;
    }
}
