package view;

import controller.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;

import java.util.regex.Pattern;

public class RegisterController extends AppController {

    @FXML
    private TextField usernameField;

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
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!validateUsername(username)) {
            showAlert("Error", "Invalid Username", "Username can only contain uppercase and lowercase letters, numbers, and the '-' character.");
            return;
        }

        if (isUsernameTaken(username)) {
            showAlert("Error", "Duplicate Username", "Username already exists. Please choose a different username.");
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

        // Create the new user and save it to the system (implementation depends on your system)
        User newUser = new User(username, email, password);
        saveUser(newUser);

        showAlert("Success", "Registration Successful", "Registration completed successfully.");
        // Navigate to the next screen or close the registration form
    }

    private boolean validateUsername(String username) {
        return Pattern.matches("^[a-zA-Z0-9-]+$", username);
    }

    private boolean isUsernameTaken(String username) {
        // Implement your logic to check if the username is already taken
        // For example, search in the database or a list of users
        return false;
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

    private void saveUser(User user) {
        // Implement your logic to save the user to the system
        // For example, save to a database or a list of users
    }
}
