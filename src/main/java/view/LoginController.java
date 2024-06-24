package view;

import controller.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.DatabaseConnection;

import java.sql.SQLException;

public class LoginController extends AppController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button forgotPasswordButton;

    @FXML
    private Button registerButton;

    @FXML
    public void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            if (!DatabaseConnection.isUsernameTaken(username)) {
                showAlert("Error", "Invalid Username", "The entered username does not exist.");
                return;
            }

            if (!DatabaseConnection.checkPassword(username, password)) {
                showAlert("Error", "Invalid Password", "The entered password is incorrect.");
                return;
            }

            showAlert("Success", "Login Successful", "You have successfully logged in.");
            // Navigate to the main menu
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while logging in. Please try again.");
        }
    }

    @FXML
    public void handleForgotPasswordButtonAction(ActionEvent event) {
        loadScene("/fxml/f.fxml");
    }

    @FXML
    public void handleRegisterButtonAction(ActionEvent event) {
        // Navigate to the registration screen
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
