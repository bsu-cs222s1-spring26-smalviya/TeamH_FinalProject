package UI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class SavedRecipesView {

    private final ListView<String> savedList = new ListView<>();
    private final Button backButton = new Button("Back");
    private final Button deleteButton = new Button("Delete Selected");
    private final VBox root = new VBox(10);

    public SavedRecipesView() {
        buildUI();
    }

    private void buildUI() {
        deleteButton.setDisable(true);

        root.getChildren().addAll(
                savedList,
                deleteButton,
                backButton
        );

        root.setPadding(new Insets(10));
    }

    public VBox getRoot() {
        return root;
    }

    public ListView<String> getSavedList() {
        return savedList;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }
}
