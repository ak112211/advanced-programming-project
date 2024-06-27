package view;

import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.App;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;

public class RegisterMenuController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;


    @FXML
    private void handleRegisterButtonAction() {
        String username = usernameField.getText();
        String nickname = nicknameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        try {
            if (DatabaseConnection.isUsernameTaken(username)) {
                Tools.showAlert("Username is already taken.");
            } else if (!confirmPassword.equals(password)) {
                Tools.showAlert("Those passwords didn’t match. Try again.");
            } else {
                User user = new User(username, nickname, email, password);
                DatabaseConnection.saveUser(user);
                Tools.showAlert("User registered successfully.");
            }
        } catch (SQLException e) {
            Tools.showAlert("Error registering user: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        App.loadScene(Menu.LOGIN_MENU.getPath());
    }
}
