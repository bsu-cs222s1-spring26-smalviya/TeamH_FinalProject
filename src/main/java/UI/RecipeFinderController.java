package UI;

import edu.bsu.cs222.finalproject.UserStorage;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

public class RecipeFinderController {

    private Stage stage;
    private RecipeService service;
    private UserStorage storage;
    private RecipeFinderView view;

    public void initialize(Stage stage, RecipeService service, UserStorage storage, RecipeFinderView view) {
        this.stage = stage;
        this.service = service;
        this.storage = storage;
        this.view = view;

        view.getSearchButton().setOnAction(e -> onSearchClicked());
        view.getSavedButton().setOnAction(e -> openSavedRecipes());
        view.getSaveButton().setOnAction(e -> saveSelectedRecipe());

        // APPLY CELL FACTORY ONCE
        applyAllergenHighlighting(view.getResultsList());
    }

    private void onSearchClicked() {

        String rawIngredients = view.getIngredientField().getText().trim();
        String rawAllergies = view.getAllergyField().getText().trim();

        if (rawIngredients.isBlank()) {
            view.getResultsList().getItems().setAll("Please enter at least one ingredient.");
            return;
        }

        String[] ingredients = service.parseIngredients(rawIngredients);
        String[] allergies = service.parseAllergies(rawAllergies);

        service.setAllergies(allergies);

        List<String> results = null;

        for (String ing : ingredients) {
            ing = service.normalizeIngredient(ing);

            String[] found = service.searchByIngredient(ing);

            if (results == null) {
                results = new ArrayList<>(Arrays.asList(found));
            } else {
                results.retainAll(Arrays.asList(found));
            }
        }

        if (results == null || results.isEmpty()) {
            view.getResultsList().getItems().setAll("No recipes match ALL ingredients.");
        } else {
            view.getResultsList().getItems().setAll(results);
        }

        view.getResultsList().refresh();

        view.getResultsList().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) openRecipeDetails();
        });
    }

    private void saveSelectedRecipe() {
        String selected = view.getResultsList().getSelectionModel().getSelectedItem();
        if (selected == null || !selected.contains("|")) return;

        storage.addRecipe(selected);
    }

    private void openRecipeDetails() {
        String selected = view.getResultsList().getSelectionModel().getSelectedItem();
        if (selected == null || !selected.contains("|")) return;

        String id = service.extractId(selected);
        String details = service.getMealDetails(id, service.getAllergies());

        RecipePopup.display(selected, details);
    }

    private void openSavedRecipes() {
        SavedRecipesView savedView = new SavedRecipesView();
        SavedRecipesController controller = new SavedRecipesController();
        controller.initialize(stage, service, storage, savedView);

        Scene scene = new Scene(savedView.getRoot(), 450, 500);
        stage.setScene(scene);
    }

    // FIXED: CELL FACTORY APPLIED ONCE, ALWAYS CHECKS INGREDIENTS
    private void applyAllergenHighlighting(javafx.scene.control.ListView<String> list) {
        list.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(item);

                if (!item.contains("|")) {
                    setStyle("");
                    return;
                }

                String id = item.substring(item.indexOf("|") + 1).trim();

                boolean containsAllergen = service.recipeContainsAllergen(id, service.getAllergies());

                if (containsAllergen) {
                    setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                } else {
                    setStyle("");
                }
            }
        });
    }
}
