package UI;

import edu.bsu.cs222.finalproject.JsonDataParser;
import edu.bsu.cs222.finalproject.RecipeGrabber;
import edu.bsu.cs222.finalproject.UserStorage;
import javafx.application.Platform;
import javafx.stage.Stage;

public class SavedRecipesController {

    private final SavedRecipesView view;
    private final UserStorage storage;
    private final String[] allergyList;
    private final Stage stage;
    private final Runnable onBack;

    public SavedRecipesController(
            SavedRecipesView view,
            UserStorage storage,
            String[] allergyList,
            Stage stage,
            Runnable onBack
    ) {
        this.view = view;
        this.storage = storage;
        this.allergyList = allergyList;
        this.stage = stage;
        this.onBack = onBack;

        wireEvents();
        loadSavedRecipes();
    }

    private void wireEvents() {
        view.getBackButton().setOnAction(event -> onBack.run());
        view.getDeleteButton().setOnAction(event -> handleDelete());
        view.getSavedList().setOnMouseClicked(event -> handleRecipeClick());
    }

    private void loadSavedRecipes() {
        var saved = storage.getSavedRecipes();
        view.getSavedList().getItems().setAll(saved);

        view.getDeleteButton().setDisable(saved.isEmpty());
    }

    private void handleDelete() {
        String selected = view.getSavedList().getSelectionModel().getSelectedItem();
        if (selected == null) return;

        storage.deleteRecipe(selected);
        loadSavedRecipes();
    }

    private void handleRecipeClick() {
        String selected = view.getSavedList().getSelectionModel().getSelectedItem();
        if (selected == null || !selected.contains("|")) return;

        String[] parts = selected.split("\\|");
        String mealName = parts[0];
        String id = parts[1];

        view.getSavedList().getItems().setAll("Loading recipe details...");

        new Thread(() -> loadRecipeDetails(id, mealName)).start();
    }

    private void loadRecipeDetails(String id, String mealName) {
        try {
            RecipeGrabber grabber = new RecipeGrabber();
            JsonDataParser parser = new JsonDataParser();

            String json = grabber.fetchRecipeById(id);
            String details = parser.parseMealDetails(json);

            String highlighted = parser.highlightAllergensInText(details, allergyList);

            Platform.runLater(() ->
                    view.getSavedList().getItems().setAll(highlighted.split("\n"))
            );

        } catch (Exception e) {
            Platform.runLater(() ->
                    view.getSavedList().getItems().setAll("Error loading recipe details.")
            );
        }
    }
}
