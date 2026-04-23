package UI;

import edu.bsu.cs222.finalproject.UserStorage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RecipeAppSkeleton extends Application {

    @Override
    public void start(Stage stage) {

        UserStorage storage = new UserStorage();
        RecipeService service = new RecipeService();

        LoginScreen login = new LoginScreen(storage);

        Runnable onLoginSuccess = () -> {
            RecipeFinderView finderView = new RecipeFinderView();
            RecipeFinderController controller = new RecipeFinderController();

            // FIX: pass storage into initialize()
            controller.initialize(stage, service, storage, finderView);

            Scene finderScene = new Scene(finderView.getRoot(), 600, 500);
            stage.setScene(finderScene);
        };

        Scene loginScene = new Scene(login.getView(onLoginSuccess), 400, 300);

        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
