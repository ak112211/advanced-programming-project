package view;

import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.App;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;
import java.util.List;

public class ProfileMenuController {
    private static final int defaultNumberToShowGameHistory = 5;

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
    private Label highScoreLabel;
    @FXML
    private Label rankLabel;
    @FXML
    private Label gamesPlayedLabel;
    @FXML
    private Label drawsLabel;
    @FXML
    private Label winsLabel;
    @FXML
    private Label lossesLabel;
    @FXML
    private TextField gameHistoryField;


    @FXML
    private void initialize() {
        try {
            User user = User.getCurrentUser();
            usernameField.setText(user.getUsername());
            nicknameField.setText(user.getNickname());
            emailField.setText(user.getEmail());
            passwordField.setText(user.getPassword());
            confirmPasswordField.setText(user.getPassword());

            highScoreLabel.setText(String.valueOf(user.getHighScore()));
            rankLabel.setText(String.valueOf(user.getRank()));
            gamesPlayedLabel.setText(String.valueOf(user.getGames().size()));
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
    private void handleShowGameHistory() {
        String numberString = gameHistoryField.getText();
        int number = defaultNumberToShowGameHistory;
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
    private void handleUpdateButtonAction() throws SQLException {
        User user = User.getCurrentUser();
        String username = usernameField.getText();
        String nickname = nicknameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!user.getUsername().equals(username)) {
            if (DatabaseConnection.getUser(username) != null) {
                String newUsername = Tools.suggestNewUsername(username);
                Tools.showAlert("Error", "Username Taken", "Username is already taken. Suggested username: " + newUsername);
            } else {
                
            }
        }
        if (!user.getNickname().equals(nickname)) {

        }
        if (!user.getEmail().equals(email)) {

        }
        if (!user.getPassword().equals(password)) {

        }
    }

    @FXML
    private void handleBack() {
        App.loadScene(Menu.MAIN_MENU.getPath());
    }

}
