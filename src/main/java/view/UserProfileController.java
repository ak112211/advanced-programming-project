package view;

import controller.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;
import java.util.List;

public class UserProfileController extends AppController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label nicknameLabel;

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
    private TextField recentGamesField;

    @FXML
    public void initialize() {
        try {
            User currentUser = User.getCurrentUser();
            usernameLabel.setText(currentUser.getUsername());
            nicknameLabel.setText(currentUser.getNickname());
            highScoreLabel.setText(String.valueOf(currentUser.getHighScore()));
            rankLabel.setText(String.valueOf(currentUser.getRank()));
            gamesPlayedLabel.setText(String.valueOf(currentUser.getGames().size()));
            drawsLabel.setText(String.valueOf(DatabaseConnection.getDrawsCount(currentUser.getUsername())));
            winsLabel.setText(String.valueOf(DatabaseConnection.getWinsCount(currentUser.getUsername())));
            lossesLabel.setText(String.valueOf(DatabaseConnection.getLossesCount(currentUser.getUsername())));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while fetching the user profile. Please try again.");
        }
    }

    @FXML
    public void handleShowRecentGamesAction(ActionEvent event) {
        String nStr = recentGamesField.getText();
        int n = 5; // default value
        if (!nStr.isEmpty()) {
            try {
                n = Integer.parseInt(nStr);
                if (n < 1) {
                    showAlert("Error", "Invalid Input", "The number of recent games must be a positive integer greater than zero.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid Input", "The number of recent games must be a positive integer greater than zero.");
                return;
            }
        }

        try {
            List<String> recentGames = DatabaseConnection.getRecentGames(User.getCurrentUser().getUsername(), n);
            if (recentGames.isEmpty()) {
                showAlert("Information", "No Games Found", "No recent games found for the user.");
            } else {
                // Show recent games (This can be improved by showing in a new window or a list view)
                StringBuilder sb = new StringBuilder();
                for (String game : recentGames) {
                    sb.append(game).append("\n");
                }
                showAlert("Recent Games", "Recent Games History", sb.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while fetching the recent games. Please try again.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
