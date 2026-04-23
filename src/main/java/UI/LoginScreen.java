package UI;

import edu.bsu.cs222.finalproject.UserStorage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginScreen {

    private final UserStorage storage;

    public LoginScreen(UserStorage storage) {
        this.storage = storage;
    }

    public VBox getView(Runnable onLoginSuccess) {
        TextField usernameField = buildUsernameField();
        PasswordField passwordField = buildPasswordField();
        Label messageLabel = new Label();

        Button loginButton = buildLoginButton(usernameField, passwordField, messageLabel, onLoginSuccess);
        Button createAccountButton = buildCreateAccountButton(usernameField, passwordField, messageLabel);

        return assembleLayout(usernameField, passwordField, loginButton, createAccountButton, messageLabel);
    }

    private TextField buildUsernameField() {
        TextField field = new TextField();
        field.setPromptText("Username");
        return field;
    }

    private PasswordField buildPasswordField() {
        PasswordField field = new PasswordField();
        field.setPromptText("Password");
        return field;
    }

    private Button buildLoginButton(
            TextField usernameField,
            PasswordField passwordField,
            Label messageLabel,
            Runnable onLoginSuccess
    ) {
        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> handleLogin(usernameField, passwordField, messageLabel, onLoginSuccess));
        return loginButton;
    }

    private Button buildCreateAccountButton(
            TextField usernameField,
            PasswordField passwordField,
            Label messageLabel
    ) {
        Button createButton = new Button("Create Account");
        createButton.setOnAction(event -> handleAccountCreation(usernameField, passwordField, messageLabel));
        return createButton;
    }

    private VBox assembleLayout(
            TextField usernameField,
            PasswordField passwordField,
            Button loginButton,
            Button createAccountButton,
            Label messageLabel
    ) {
        return new VBox(
                10,
                usernameField,
                passwordField,
                loginButton,
                createAccountButton,
                messageLabel
        );
    }

    private void handleLogin(
            TextField usernameField,
            PasswordField passwordField,
            Label messageLabel,
            Runnable onLoginSuccess
    ) {
        boolean loginSuccessful = storage.login(
                usernameField.getText(),
                passwordField.getText()
        );

        if (loginSuccessful) {
            messageLabel.setText("Login successful!");
            onLoginSuccess.run();
        } else {
            messageLabel.setText("Invalid username or password");
        }
    }

    private void handleAccountCreation(
            TextField usernameField,
            PasswordField passwordField,
            Label messageLabel
    ) {
        boolean accountCreated = storage.createAccount(
                usernameField.getText(),
                passwordField.getText()
        );

        if (accountCreated) {
            messageLabel.setText("Account created! You can now log in.");
        } else {
            messageLabel.setText("Username already exists");
        }
    }
}
