package view;

import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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
    private CheckBox showPasswordCheckBox;

    @FXML
    private void initialize() {
        passwordField.setVisible(false);
        showPasswordCheckBox.setSelected(false);
    }

    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user;
        try {
            if ((user = DatabaseConnection.getUser(username)) == null) {
                Tools.showAlert("Invalid username.");
            } else if (DatabaseConnection.checkPassword(username, password)) {
                User.setCurrentUser(user);
                App.loadScene(Menu.MAIN_MENU.getPath());
                Tools.showAlert("Login successful. Welcome " + user.getNickname() + "!");
            } else {
                Tools.showAlert("Invalid password.");
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

    @FXML
    private void handleShowPassword() {
        passwordField.setVisible(showPasswordCheckBox.isSelected());
    }

}
