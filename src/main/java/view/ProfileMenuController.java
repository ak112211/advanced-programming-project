package view;

import enums.Menu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.App;
import model.User;
import util.DatabaseConnection;

public class ProfileMenuController {

    @FXML
    private TextField currentUsernameField;
    @FXML
    private TextField newUsernameField;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField emailField;

    @FXML
    private void initialize() {
        User currentUser = User.getCurrentUser();
        currentUsernameField.setText(currentUser.getUsername());
        nicknameField.setText(currentUser.getNickname());
        emailField.setText(currentUser.getEmail());
    }

    @FXML
    private void handleUpdateButtonAction() {
        String currentUsername = currentUsernameField.getText();
        String newUsername = newUsernameField.getText();
        String nickname = nicknameField.getText();
        String email = emailField.getText();

        // Check for empty fields and validate input
        if (currentUsername.isEmpty() || newUsername.isEmpty() || nickname.isEmpty() || email.isEmpty()) {
            Tools.showAlert("Error", "Input Error", "Please fill in all fields.");
            return;
        }

        // Update profile in the database
        boolean isProfileUpdated = DatabaseConnection.updateUserProfile(currentUsername, newUsername, nickname, email);

        if (isProfileUpdated) {
            Tools.showAlert("Success", "Profile Updated", "Your profile has been updated successfully.");
        } else {
            Tools.showAlert("Error", "Database Error", "An error occurred while updating the profile. Please try again.");
        }
    }

    @FXML
    public void handleBack() {
        App.loadScene(Menu.MAIN_MENU.getPath());
    }
}
