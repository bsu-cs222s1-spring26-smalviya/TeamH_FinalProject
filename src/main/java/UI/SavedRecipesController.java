package UI;

import edu.bsu.cs222.finalproject.UserStorage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SavedRecipesController {

    private final SavedRecipesView view;
    private final UserStorage storage;
    private final RecipeService service;
    private final String allergyRaw;

    public SavedRecipesController(SavedRecipesView view, UserStorage storage, RecipeService service, String allergyRaw) {
        this.view = view;
        this.storage = storage;
        this.service = service;
        this.allergyRaw = allergyRaw;

        setupListCellRenderer();
        loadSavedRecipes();
        setupHandlers();
    }

    private void loadSavedRecipes() {
        String[] allergies = service.parseAllergies(allergyRaw);

        view.getSavedList().getItems().clear();

        for (String recipe : storage.getSavedRecipes()) {
            String clean = recipe;
            String id = service.extractId(clean);

            String details = service.getMealDetails(id, allergies).toLowerCase();

            boolean contains = false;
            for (String allergy : allergies) {
                if (!allergy.isBlank() && details.contains(allergy.trim())) {
                    contains = true;
                    break;
                }
            }

            if (contains) {
                view.getSavedList().getItems().add("[RED]" + recipe + "[/RED]");
            } else {
                view.getSavedList().getItems().add(recipe);
            }
        }
    }

    private void setupHandlers() {

        view.getDeleteButton().setOnAction(e -> {
            String selected = view.getSavedList().getSelectionModel().getSelectedItem();
            if (selected != null) {
                String clean = selected.replace("[RED]", "").replace("[/RED]", "");
                storage.removeRecipe(clean);
                loadSavedRecipes();
            }
        });

        view.getViewButton().setOnAction(e -> {
            String selected = view.getSavedList().getSelectionModel().getSelectedItem();
            if (selected != null) {

                String clean = selected.replace("[RED]", "").replace("[/RED]", "");
                String id = service.extractId(clean);

                String[] allergies = service.parseAllergies(allergyRaw);
                String details = service.getMealDetails(id, allergies);

                showRecipePopup(details);
            }
        });

        view.getBackButton().setOnAction(e -> {
            RecipeFinderView finderView = new RecipeFinderView();
            new RecipeFinderController(finderView, storage, service, (Stage) view.getRoot().getScene().getWindow());
            ((Stage) view.getRoot().getScene().getWindow()).setScene(new Scene(finderView.getRoot(), 600, 600));
        });
    }

    // LISTVIEW RENDERER

    private void setupListCellRenderer() {
        view.getSavedList().setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                TextFlow flow = buildColoredText(item);
                setGraphic(flow);
                setText(null);
            }
        });
    }

    // POPUP + SAFE PARSER

    private void showRecipePopup(String details) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Recipe Details");

        TextFlow flow = buildColoredText(details);
        alert.getDialogPane().setContent(flow);
        alert.show();
    }

    private TextFlow buildColoredText(String details) {
        TextFlow flow = new TextFlow();
        flow.setPrefWidth(500);

        int index = 0;

        while (index < details.length()) {
            int start = details.indexOf("[RED]", index);

            if (start == -1) {
                flow.getChildren().add(new Text(details.substring(index)));
                break;
            }

            if (start > index) {
                flow.getChildren().add(new Text(details.substring(index, start)));
            }

            int end = details.indexOf("[/RED]", start);
            if (end == -1) {
                flow.getChildren().add(new Text(details.substring(start)));
                break;
            }

            String redText = details.substring(start + 5, end);
            Text t = new Text(redText);
            t.setFill(Color.RED);
            t.setStyle("-fx-font-weight: bold;");
            flow.getChildren().add(t);

            index = end + 6;
        }

        return flow;
    }
}
