package UI;

import edu.bsu.cs222.finalproject.UserStorage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class RecipeFinderController {

    private final RecipeFinderView view;
    private final UserStorage storage;
    private final RecipeService service;
    private final Stage stage;

    public RecipeFinderController(RecipeFinderView view, UserStorage storage, RecipeService service, Stage stage) {
        this.view = view;
        this.storage = storage;
        this.service = service;
        this.stage = stage;

        setupHandlers();
        setupListCellRenderer();
    }

    private void setupHandlers() {

        view.getSearchButton().setOnAction(e -> {
            String ingredient = view.getIngredientField().getText().trim();
            view.getResultsList().getItems().clear();
            view.getResultsList().getSelectionModel().clearSelection();

            String[] results = service.searchByIngredient(ingredient);
            String[] allergies = service.parseAllergies(view.getAllergyField().getText());
            String[] ingredient2 = service.parseIngredients(ingredient);

            for (int k = 1; k < ingredient2.length; k++){//for testing ingredient2 value
                System.out.println(ingredient2[k]);
            }
            if (ingredient2.length > 1){
                for (int i = 1; i < results.length; i++){
                    String containsBoth = results[i];
                    String id = service.extractId(containsBoth);

                    String fullDetails = service.getMealDetails(id, results).toLowerCase();

                    boolean containsIngredient2 = false;

                    for (String contains : ingredient2) {
                        if (!contains.isBlank() && fullDetails.contains(contains.trim())) {
                            containsIngredient2 = true;
                            break;
                        }
                    }
                    if (containsIngredient2) {
                        results[i] =  "Contains both: " + containsBoth;
                    }

                }
            }

            // Highlight entire recipe name if allergen appears ANYWHERE in the recipe

            for (int i = 0; i < results.length; i++) {
                String clean = results[i];
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
                    results[i] = "[RED]" + clean + "[/RED]";
                }
            }


            view.getResultsList().getItems().setAll(results);
        });

        view.getSaveButton().setOnAction(e -> {
            String selected = view.getResultsList().getSelectionModel().getSelectedItem();
            if (selected != null && storage.isLoggedIn()) {
                String clean = selected.replace("[RED]", "").replace("[/RED]", "");
                storage.addRecipe(clean);
            }
        });

        view.getResultsList().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                String selected = view.getResultsList().getSelectionModel().getSelectedItem();
                if (selected != null) {

                    String clean = selected.replace("[RED]", "").replace("[/RED]", "");
                    String id = service.extractId(clean);

                    String[] allergies = service.parseAllergies(view.getAllergyField().getText());
                    String details = service.getMealDetails(id, allergies);

                    showRecipePopup(details);
                }
            }
        });

        view.getSavedButton().setOnAction(e -> {
            SavedRecipesView savedView = new SavedRecipesView();
            new SavedRecipesController(savedView, storage, service, view.getAllergyField().getText());
            stage.setScene(new Scene(savedView.getRoot(), 600, 600));
        });
    }

    // LISTVIEW RENDERER (NO RAW TAGS)

    private void setupListCellRenderer() {
        view.getResultsList().setCellFactory(list -> new ListCell<>() {
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
