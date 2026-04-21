package UI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class RecipeFinderView {

    private final TextField ingredientField = new TextField();
    private final TextField allergyField = new TextField();
    private final Button searchButton = new Button("Search");
    private final Button saveButton = new Button("Save Recipe");
    private final Button viewSavedButton = new Button("View Saved Recipes");
    private final Button logoutButton = new Button("Logout");
    private final ListView<String> resultsList = new ListView<>();
    private final VBox root = new VBox(10);

    public RecipeFinderView() {
        buildUI();
    }

    private void buildUI() {
        ingredientField.setPromptText("Search for an ingredient");
        allergyField.setPromptText("Allergies (comma-separated)");
        saveButton.setDisable(true);

        resultsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else if (item.contains("[RED]")) {
                    setText(item.replace("[RED]", "").replace("[/RED]", ""));
                    setStyle("-fx-text-fill: red;");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: black;");
                }
            }
        });

        root.getChildren().addAll(
                ingredientField,
                allergyField,
                searchButton,
                saveButton,
                viewSavedButton,
                logoutButton,
                resultsList
        );

        root.setPadding(new Insets(10));
    }

    public VBox getRoot() {
        return root;
    }

    public TextField getIngredientField() {
        return ingredientField;
    }

    public TextField getAllergyField() {
        return allergyField;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getViewSavedButton() {
        return viewSavedButton;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }

    public ListView<String> getResultsList() {
        return resultsList;
    }
}
