package UI;

import edu.bsu.cs222.finalproject.UserStorage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {

    private final UserStorage storage;

    public LoginScreen(UserStorage storage) {
        this.storage = storage;
    }

    public VBox getView(Stage stage, Runnable onLoginSuccess) {

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button createButton = new Button("Create Account");

        Label message = new Label();

        loginButton.setOnAction(e -> {
            boolean success = storage.login(
                    usernameField.getText(),
                    passwordField.getText()
            );

            if (success) {
                message.setText("Login successful!");
                onLoginSuccess.run(); // SWITCH TO RECIPE FINDER
            } else {
                message.setText("Invalid username or password");
            }
        });

        createButton.setOnAction(e -> {
            boolean created = storage.createAccount(
                    usernameField.getText(),
                    passwordField.getText()
            );

            if (created) {
                message.setText("Account created! You can now log in.");
            } else {
                message.setText("Username already exists");
            }
        });

        return new VBox(10, usernameField, passwordField, loginButton, createButton, message);
    }
}
