package view;

import controller.AppController;
import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.App;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;
import java.util.List;

import static view.Tools.showAlert;

public class ProfileMenuController {
    private static final int DEFAULT_NUMBER_TO_SHOW_GAME_HISTORY = 5;

    @FXML
    public TextField usernameField;
    @FXML
    public TextField nicknameField;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField oldPasswordField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public Label highScoreLabel;
    @FXML
    public Label rankLabel;
    @FXML
    public Label gamesPlayedLabel;
    @FXML
    public Label drawsLabel;
    @FXML
    public Label winsLabel;
    @FXML
    public Label lossesLabel;
    @FXML
    public TextField gameHistoryField;
    @FXML
    public CheckBox isTwoFactorOn;

    @FXML
    public void initialize() {
        try {
            User user = User.getCurrentUser();
            usernameField.setText(user.getUsername());
            nicknameField.setText(user.getNickname());
            emailField.setText(user.getEmail());
            isTwoFactorOn.setSelected(user.isTwoFactorOn());
            highScoreLabel.setText(String.valueOf(user.getHighScore()));
            rankLabel.setText(String.valueOf(user.getRank()));
            gamesPlayedLabel.setText(String.valueOf(user.getGames() != null ? user.getGames().size() : 0));
            int drawsResponse = DatabaseConnection.getDrawsCount(user.getUsername());
            drawsLabel.setText(String.valueOf(drawsResponse));
            int winsResponse = DatabaseConnection.getWinsCount(user.getUsername());
            winsLabel.setText(String.valueOf(winsResponse));
            int lossesResponse = DatabaseConnection.getLossesCount(user.getUsername());
            lossesLabel.setText(String.valueOf(lossesResponse));

        } catch (Exception e) {
            e.printStackTrace();
            Tools.showAlert("Error", "Database Error", "An error occurred while fetching the user profile. Please try again.");
        }
    }

    @FXML
    public void handleShowGameHistory() {
        String numberString = gameHistoryField.getText();
        int number = DEFAULT_NUMBER_TO_SHOW_GAME_HISTORY;
        if (!numberString.isEmpty()) {
            try {
                number = Integer.parseInt(numberString);
                if (number < 1) {
                    Tools.showAlert("Error", "Invalid Input", "The number of recent games must be a positive integer greater than zero.");
                    return;
                }
            } catch (NumberFormatException e) {
                Tools.showAlert("Error", "Invalid Input", "The number of recent games must be a positive integer greater than zero.");
                return;
            }
        }

        try {
            String username = User.getCurrentUser().getUsername();
            List<String> recentGames = DatabaseConnection.getRecentGames(username, number);
            if (recentGames.isEmpty()) {
                Tools.showAlert("Information", "No Games Found", "No recent games found for the user.");
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                for (String game : recentGames) {
                    stringBuilder.append(game).append("\n");
                }
                Tools.showAlert("Recent Games", "Recent Games History", stringBuilder.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Tools.showAlert("Error", "Database Error", "An error occurred while fetching the recent games. Please try again.");
        }
    }

    @FXML
    public void handleUpdateButtonAction() throws SQLException {
        User user = User.getCurrentUser();
        String oldUsername = user.getUsername();
        String username = usernameField.getText();
        String nickname = nicknameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!Tools.isValidUsername(username)) {
            Tools.showAlert("Error", "Invalid Username", "Username must be alphanumeric and can include dashes.");
            return;
        }

        if (!Tools.isValidEmail(email)) {
            Tools.showAlert("Error", "Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (!Tools.isValidPassword(password)) {
            showAlert("Error", "Weak Password", "Invalid password. Password must be at least 8 characters long, include uppercase, lowercase, numbers, and special characters.");
            return;
        }

        if (!user.getPassword().equals(oldPasswordField.getText())) {
            showAlert("Error", "Password incorrect", "Current password incorrect.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            Tools.showAlert("Error", "Password Mismatch", "Password and confirm password do not match.");
            return;
        }

        boolean isUpdated = false;

        if (!user.getUsername().equals(username)) {
            if (DatabaseConnection.getUser(username) != null) {
                String newUsername = Tools.suggestNewUsername(username);
                Tools.showAlert("Error", "Username Taken", "Username is already taken. Suggested username: " + newUsername);
                return;
            } else {
                user.setUsername(username);
                isUpdated = true;
            }
        }

        if (!user.getNickname().equals(nickname)) {
            user.setNickname(nickname);
            isUpdated = true;
        }

        if (!user.getEmail().equals(email)) {
            user.setEmail(email);
            isUpdated = true;
        }

        if (user.isTwoFactorOn() != isTwoFactorOn.isSelected()) {
            user.setEmail(email);
            isUpdated = true;
        }

        if (!user.getPassword().equals(password)) {
            user.setPassword(password);
            isUpdated = true;
        }

        if (isUpdated) {
            DatabaseConnection.updateUserProfile(user, oldUsername, isTwoFactorOn.isSelected());
            Tools.showAlert("Success", "Profile Updated", "Your profile has been updated successfully.");
        } else {
            Tools.showAlert("Information", "No Changes", "No changes detected to update.");
        }
    }

    @FXML
    public void handleBack() {
        AppController.loadScene(Menu.MAIN_MENU);
    }
}
