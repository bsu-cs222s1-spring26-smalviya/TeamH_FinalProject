package UI;

import edu.bsu.cs222.finalproject.UserStorage;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class SavedRecipesController {

    private Stage stage;
    private RecipeService service;
    private UserStorage storage;
    private SavedRecipesView view;

    public void initialize(Stage stage, RecipeService service, UserStorage storage, SavedRecipesView view) {
        this.stage = stage;
        this.service = service;
        this.storage = storage;
        this.view = view;

        loadSavedRecipes();

        applyAllergenHighlighting(view.getSavedList());

        view.getBackButton().setOnAction(e -> goBack());
        view.getViewButton().setOnAction(e -> openRecipe());
        view.getDeleteButton().setOnAction(e -> deleteSelected());

        view.getSavedList().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) openRecipe();
        });
    }

    private void loadSavedRecipes() {
        List<String> saved = storage.getSavedRecipes();
        view.getSavedList().getItems().setAll(saved);
        view.getSavedList().refresh();
    }

    private void goBack() {
        RecipeFinderView finderView = new RecipeFinderView();
        RecipeFinderController controller = new RecipeFinderController();
        controller.initialize(stage, service, storage, finderView);

        Scene scene = new Scene(finderView.getRoot(), 600, 500);
        stage.setScene(scene);
    }

    private void openRecipe() {
        String selected = view.getSavedList().getSelectionModel().getSelectedItem();
        if (selected == null || !selected.contains("|")) return;

        String id = service.extractId(selected);
        String details = service.getMealDetails(id, service.getAllergies());

        RecipePopup.display(selected, details);
    }

    private void deleteSelected() {
        String selected = view.getSavedList().getSelectionModel().getSelectedItem();
        if (selected == null) return;

        storage.removeRecipe(selected);
        loadSavedRecipes();
    }

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
