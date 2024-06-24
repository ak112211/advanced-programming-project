package view;

import controller.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class ProfileController extends AppController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField nicknameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button updateButton;

    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = User.getCurrentUser();
        usernameField.setText(currentUser.getUsername());
        nicknameField.setText(currentUser.getNickname());
        emailField.setText(currentUser.getEmail());
    }

    @FXML
    public void handleUpdateButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String nickname = nicknameField.getText();
        String email = emailField.getText();
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        try {
            if (!username.equals(currentUser.getUsername()) && DatabaseConnection.isUsernameTaken(username)) {
                showAlert("Error", "Duplicate Username", "The username already exists. Please choose a different username.");
                return;
            }

            if (username.equals(currentUser.getUsername()) && nickname.equals(currentUser.getNickname()) && email.equals(currentUser.getEmail())) {
                showAlert("Error", "No Changes Detected", "The new values cannot be the same as the current values.");
                return;
            }

            if (!validateEmail(email)) {
                showAlert("Error", "Invalid Email", "The entered email address is invalid.");
                return;
            }

            if (!oldPassword.isEmpty() || !newPassword.isEmpty() || !confirmPassword.isEmpty()) {
                if (!DatabaseConnection.checkPassword(currentUser.getUsername(), oldPassword)) {
                    showAlert("Error", "Invalid Old Password", "The old password is incorrect.");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    showAlert("Error", "Password Mismatch", "New password and its confirmation do not match.");
                    return;
                }

                if (newPassword.equals(oldPassword)) {
                    showAlert("Error", "Same as Old Password", "The new password cannot be the same as the old password.");
                    return;
                }

                if (!validatePassword(newPassword)) {
                    showAlert("Error", "Invalid Password", "Password can only contain special characters, numbers, and uppercase and lowercase letters.");
                    return;
                }

                if (!isStrongPassword(newPassword)) {
                    showAlert("Error", "Weak Password", "Password must be at least 8 characters long and include uppercase and lowercase letters, numbers, and special characters.");
                    return;
                }

                DatabaseConnection.updatePassword(currentUser.getUsername(), newPassword);
            }

            DatabaseConnection.updateUserProfile(currentUser.getUsername(), username, nickname, email);
            currentUser.setUsername(username);
            currentUser.setNickname(nickname);
            currentUser.setEmail(email);

            showAlert("Success", "Profile Updated", "Your profile has been updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while updating the profile. Please try again.");
        }
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
