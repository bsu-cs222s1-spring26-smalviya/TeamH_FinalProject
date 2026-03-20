package UI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RecipeAppSkeleton extends Application {

    @Override
    public void start(Stage stage) {
        // UI elements
        Label title = new Label("Recipe Finder");
        TextField searchField = new TextField();
        searchField.setPromptText("Search for an ingredient...");

        Button searchButton = new Button("Search");

        ListView<String> resultsList = new ListView<>();
        resultsList.getItems().add("Results will appear here...");

        // Placeholder action
        searchButton.setOnAction(e -> {
            resultsList.getItems().clear();
            resultsList.getItems().add("Searching for: " + searchField.getText());
            resultsList.getItems().add("(API integration coming in Iteration 2)");
        });

        VBox layout = new VBox(10, title, searchField, searchButton, resultsList);

        Scene scene = new Scene(layout, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Recipe Finder");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
