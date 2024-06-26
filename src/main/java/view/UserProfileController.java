package view;

import enums.Menu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.App;
import model.User;

import java.util.Arrays;
import java.util.List;

public class UserProfileController {

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

            String drawsResponse = App.getServerConnection().sendRequest("getDrawsCount " + currentUser.getUsername());
            drawsLabel.setText(drawsResponse);

            String winsResponse = App.getServerConnection().sendRequest("getWinsCount " + currentUser.getUsername());
            winsLabel.setText(winsResponse);

            String lossesResponse = App.getServerConnection().sendRequest("getLossesCount " + currentUser.getUsername());
            lossesLabel.setText(lossesResponse);

        } catch (Exception e) {
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
            String response = App.getServerConnection().sendRequest("getRecentGames " + User.getCurrentUser().getUsername() + " " + n);
            if (response.isEmpty()) {
                showAlert("Information", "No Games Found", "No recent games found for the user.");
            } else {
                String[] recentGames = response.split("\n");
                StringBuilder sb = new StringBuilder();
                for (String game : recentGames) {
                    sb.append(game).append("\n");
                }
                showAlert("Recent Games", "Recent Games History", sb.toString());
            }
        } catch (Exception e) {
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

    public void handleBack() {
        App.loadScene(Menu.MAIN_MENU.getPath());
    }

}
