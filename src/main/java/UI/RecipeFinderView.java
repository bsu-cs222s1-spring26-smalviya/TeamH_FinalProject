package UI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class RecipeFinderView {

    private final VBox root = new VBox(10);
    private final TextField ingredientField = new TextField();
    private final TextField allergyField = new TextField();
    private final Button searchButton = new Button("Search");
    private final Button savedButton = new Button("Saved Recipes");
    private final ListView<String> resultsList = new ListView<>();
    private final Button saveButton = new Button("Save Selected");

    public RecipeFinderView() {
        ingredientField.setPromptText("Enter ingredient (comma-separated)");
        allergyField.setPromptText("Allergies (comma-separated)");

        root.setPadding(new Insets(20));
        root.getChildren().addAll(
                new Label("Ingredient:"), ingredientField,
                new Label("Allergies:"), allergyField,
                searchButton, savedButton,
                resultsList, saveButton
        );
    }

    public VBox getRoot() { return root; }
    public TextField getIngredientField() { return ingredientField; }
    public TextField getAllergyField() { return allergyField; }
    public Button getSearchButton() { return searchButton; }
    public Button getSavedButton() { return savedButton; }
    public ListView<String> getResultsList() { return resultsList; }
    public Button getSaveButton() { return saveButton; }
}
