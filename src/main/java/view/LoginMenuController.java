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
    private TextField textField;
    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    public void initialize() {
        // Bind the visibility of the textField to the showPasswordCheckBox
        textField.managedProperty().bind(showPasswordCheckBox.selectedProperty());
        textField.visibleProperty().bind(showPasswordCheckBox.selectedProperty());
        passwordField.managedProperty().bind(showPasswordCheckBox.selectedProperty().not());
        passwordField.visibleProperty().bind(showPasswordCheckBox.selectedProperty().not());

        // Bind the text properties to keep them synchronized
        textField.textProperty().bindBidirectional(passwordField.textProperty());
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
                App.getServerConnection().setLogin(User.getCurrentUser().getUsername());
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
}
