package view;

import enums.Menu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.App;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ProfileMenuController {

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
    private void initialize() {
        try {
            User currentUser = User.getCurrentUser();
            usernameLabel.setText(currentUser.getUsername());
            nicknameLabel.setText(currentUser.getNickname());
            highScoreLabel.setText(String.valueOf(currentUser.getHighScore()));
            rankLabel.setText(String.valueOf(currentUser.getRank()));
            gamesPlayedLabel.setText(String.valueOf(currentUser.getGames().size()));

            int drawsResponse = DatabaseConnection.getDrawsCount(currentUser.getUsername());
            drawsLabel.setText(String.valueOf(drawsResponse));

            int winsResponse = DatabaseConnection.getWinsCount(currentUser.getUsername());
            winsLabel.setText(String.valueOf(winsResponse));

            int lossesResponse = DatabaseConnection.getLossesCount(currentUser.getUsername());
            lossesLabel.setText(String.valueOf(lossesResponse));

        } catch (Exception e) {
            e.printStackTrace();
            Tools.showAlert("Error", "Database Error", "An error occurred while fetching the user profile. Please try again.");
        }
    }

    @FXML
    private void handleShowRecentGamesAction(ActionEvent event) {
        String nStr = recentGamesField.getText();
        int n = 5; // default value
        if (!nStr.isEmpty()) {
            try {
                n = Integer.parseInt(nStr);
                if (n < 1) {
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
            List<String> recentGames = DatabaseConnection.getRecentGames(username, n);
            if (recentGames.isEmpty()) {
                Tools.showAlert("Information", "No Games Found", "No recent games found for the user.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (String game : recentGames) {
                    sb.append(game).append("\n");
                }
                Tools.showAlert("Recent Games", "Recent Games History", sb.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Tools.showAlert("Error", "Database Error", "An error occurred while fetching the recent games. Please try again.");
        }
    }

    @FXML
    private void handleBack() {
        App.loadScene(Menu.MAIN_MENU.getPath());
    }

}
