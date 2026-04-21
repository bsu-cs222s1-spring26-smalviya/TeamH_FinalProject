package UI;

import edu.bsu.cs222.finalproject.UserStorage;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RecipeFinderController {

    private final RecipeFinderView view;
    private final RecipeService service;
    private final UserStorage storage;
    private final Stage stage;

    public RecipeFinderController(
            RecipeFinderView view,
            RecipeService service,
            UserStorage storage,
            Stage stage
    ) {
        this.view = view;
        this.service = service;
        this.storage = storage;
        this.stage = stage;

        wireEvents();
    }

    private void wireEvents() {
        view.getSearchButton().setOnAction(event -> handleSearch());
        view.getResultsList().setOnMouseClicked(event -> handleRecipeClick());
        view.getViewSavedButton().setOnAction(event -> handleViewSaved());
        view.getLogoutButton().setOnAction(event -> storage.logout());
    }

    private void handleSearch() {
        String ingredient = view.getIngredientField().getText().trim();

        if (ingredient.isEmpty()) {
            view.getResultsList().getItems().setAll("Please enter an ingredient.");
            return;
        }

        view.getResultsList().getItems().setAll("Searching...");

        new Thread(() -> {
            var results = service.searchRecipes(
                    ingredient,
                    view.getAllergyField().getText()
            );

            Platform.runLater(() ->
                    view.getResultsList().getItems().setAll(results)
            );
        }).start();
    }

    private void handleRecipeClick() {
        String selected = view.getResultsList().getSelectionModel().getSelectedItem();
        if (selected == null || !selected.contains("(ID:")) return;

        String id = service.extractId(selected);
        String mealName = selected.substring(0, selected.lastIndexOf(" (ID:"));

        view.getResultsList().getItems().setAll("Loading recipe details...");

        new Thread(() -> {
            var details = service.loadRecipeDetails(
                    id,
                    view.getAllergyField().getText()
            );

            Platform.runLater(() -> {
                view.getResultsList().getItems().setAll(details.split("\n"));
                view.getSaveButton().setDisable(false);
                view.getSaveButton().setOnAction(event -> storage.saveRecipe(mealName + "|" + id));
            });
        }).start();
    }

    private void handleViewSaved() {
        String[] allergyList = service.parseAllergies(view.getAllergyField().getText());
        SavedRecipesScreen screen = new SavedRecipesScreen(storage, allergyList);

        stage.setScene(
                screen.getScene(stage, () -> {
                    RecipeFinderView newView = new RecipeFinderView();
                    RecipeService newService = new RecipeService();
                    new RecipeFinderController(newView, newService, storage, stage);
                    stage.setScene(new Scene(newView.getRoot(), 450, 600));
                })
        );
    }
}
