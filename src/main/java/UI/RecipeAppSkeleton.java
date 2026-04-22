package UI;

import edu.bsu.cs222.finalproject.UserStorage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RecipeAppSkeleton extends Application {

    private UserStorage storage;

    @Override
    public void start(Stage stage) {
        storage = new UserStorage();
        showLoginScreen(stage);
    }

    private void showLoginScreen(Stage stage) {
        LoginScreen loginScreen = new LoginScreen(storage);
        VBox loginLayout = loginScreen.getView(() -> showRecipeFinder(stage));

        stage.setScene(new Scene(loginLayout, 300, 200));
        stage.setTitle("Login");
        stage.show();
    }

    private void showRecipeFinder(Stage stage) {
        RecipeFinderView view = new RecipeFinderView();
        RecipeService service = new RecipeService();

        new RecipeFinderController(view, storage, service, stage);

        stage.setScene(new Scene(view.getRoot(), 450, 600));
        stage.setTitle("Recipe Finder");
    }

    public static void main(String[] args) {
        launch();
    }
}
