package UI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RecipeFinderView {

    private final BorderPane root = new BorderPane();

    private final TextField ingredientField = new TextField();
    private final TextField allergyField = new TextField();
    private final Button searchButton = new Button("Search");
    private final Button saveButton = new Button("Save Recipe");
    private final Button savedButton = new Button("Saved Recipes");

    private final ListView<String> resultsList = new ListView<>();

    public RecipeFinderView() {

        ingredientField.setPromptText("Enter ingredients (comma separated)");
        allergyField.setPromptText("Enter allergies (comma separated)");

        HBox topRow = new HBox(10, ingredientField, searchButton);
        topRow.setPadding(new Insets(10));

        HBox midRow = new HBox(10, allergyField, saveButton, savedButton);
        midRow.setPadding(new Insets(10));

        VBox top = new VBox(10, topRow, midRow);

        root.setTop(top);
        root.setCenter(resultsList);
    }

    public BorderPane getRoot() { return root; }

    public TextField getIngredientField() { return ingredientField; }
    public TextField getAllergyField() { return allergyField; }
    public Button getSearchButton() { return searchButton; }
    public Button getSaveButton() { return saveButton; }
    public Button getSavedButton() { return savedButton; }
    public ListView<String> getResultsList() { return resultsList; }
}
