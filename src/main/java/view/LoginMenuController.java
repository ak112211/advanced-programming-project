package view;

import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.App;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;

public class LoginMenuController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;


    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            if (DatabaseConnection.checkPassword(username, password)) {
                User user = DatabaseConnection.getUser(username);
                User.setCurrentUser(user);
                App.loadScene(Menu.MAIN_MENU.getPath());
                Tools.showAlert("Login successful. Welcome " + user.getNickname() + "!");
            } else {
                Tools.showAlert("Invalid username or password.");
            }
        } catch (SQLException e) {
            Tools.showAlert("Error during login: " + e.getMessage());
        }
    }


    @FXML
    private void handleForgotPasswordButtonAction() {
        App.loadScene(Menu.FORGET_PASSWORD_MENU.getPath());
    }

    @FXML
    private void handleRegisterButtonAction() {
        App.loadScene(Menu.REGISTER_MENU.getPath());
    }


}
