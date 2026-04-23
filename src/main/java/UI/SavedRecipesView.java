package UI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class SavedRecipesView {

    private final ListView<String> savedList = new ListView<>();
    private final Button deleteButton = new Button("Delete Selected");
    private final Button viewButton = new Button("View Recipe");
    private final Button backButton = new Button("Back");

    private final VBox content = new VBox(12);
    private final ScrollPane root = new ScrollPane(content);

    public SavedRecipesView() {

        content.setPadding(new Insets(15));
        content.getChildren().addAll(savedList, deleteButton, viewButton, backButton);

        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        savedList.setPrefHeight(300);

        root.setPrefViewportHeight(420);
        root.setPrefViewportWidth(430);
    }

    public ScrollPane getRoot() { return root; }
    public ListView<String> getSavedList() { return savedList; }
    public Button getDeleteButton() { return deleteButton; }
    public Button getViewButton() { return viewButton; }
    public Button getBackButton() { return backButton; }
}
