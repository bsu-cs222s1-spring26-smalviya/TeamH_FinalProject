package UI;

import edu.bsu.cs222.finalproject.UserStorage;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SavedRecipesScreen {

    private final UserStorage storage;
    private final String[] allergyList;

    public SavedRecipesScreen(UserStorage storage, String[] allergyList) {
        this.storage = storage;
        this.allergyList = allergyList;
    }

    public Scene getScene(Stage stage, Runnable onBack) {
        SavedRecipesView view = new SavedRecipesView();
        new SavedRecipesController(view, storage, allergyList, stage, onBack);
        return new Scene(view.getRoot(), 450, 600);
    }
}
