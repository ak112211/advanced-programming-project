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
import java.util.regex.Pattern;

public class RegisterController extends AppController {

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
    private Button registerButton;

    @FXML
    public void handleRegisterButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String nickname = nicknameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!validateUsername(username)) {
            showAlert("Error", "Invalid Username", "Username can only contain uppercase and lowercase letters, numbers, and the '-' character.");
            return;
        }

        try {
            if (DatabaseConnection.isUsernameTaken(username)) {
                showAlert("Error", "Duplicate Username", "Username already exists. Please choose a different username.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while checking the username. Please try again.");
            return;
        }

        if (!validateEmail(email)) {
            showAlert("Error", "Invalid Email", "The entered email address is invalid.");
            return;
        }

        if (!validatePassword(password)) {
            showAlert("Error", "Invalid Password", "Password can only contain special characters, numbers, and uppercase and lowercase letters.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Password Mismatch", "Password and its confirmation do not match.");
            return;
        }

        if (!isStrongPassword(password)) {
            showAlert("Error", "Weak Password", "Password must be at least 8 characters long and include uppercase and lowercase letters, numbers, and special characters.");
            return;
        }

        // Create the new user and save it to the system
        try {
            DatabaseConnection.saveUser(username, nickname, email, password);
            showAlert("Success", "Registration Successful", "Registration completed successfully.");
            // Navigate to the next screen or close the registration form
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while saving the user. Please try again.");
        }
    }

    private boolean validateUsername(String username) {
        return Pattern.matches("^[a-zA-Z0-9-]+$", username);
    }

    private boolean validateEmail(String email) {
        return Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email);
    }

    private boolean validatePassword(String password) {
        return Pattern.matches("^[a-zA-Z0-9!@#$%^&*()_+=-]+$", password);
    }

    private boolean isStrongPassword(String password) {
        return password.length() >= 8 &&
                Pattern.matches(".*[A-Z].*", password) &&
                Pattern.matches(".*[a-z].*", password) &&
                Pattern.matches(".*[0-9].*", password) &&
                Pattern.matches(".*[!@#$%^&*()_+=-].*", password);
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
