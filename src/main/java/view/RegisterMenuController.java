package view;

import enums.Menu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.App;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;

import static view.Tools.showAlert;

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
    private void handleRegisterButtonAction() {
        String username = usernameField.getText();
        String nickname = nicknameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            if (DatabaseConnection.isUsernameTaken(username)) {
                showAlert("Username is already taken.");
            } else {
                User user = new User(username, nickname, email, password);
                DatabaseConnection.saveUser(user);
                showAlert("User registered successfully.");
            }
        } catch (SQLException e) {
            showAlert("Error registering user: " + e.getMessage());
        }
    }

    public void handleBack() {
        App.loadScene(Menu.LOGIN_MENU.getPath());
    }
}
