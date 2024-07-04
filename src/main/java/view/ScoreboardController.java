package view;

import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.App;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;
import java.util.List;

public class ScoreboardController {

    @FXML
    private ListView<String> scoreboardListView;

    @FXML
    private void initialize() {
        loadScoreboard();
    }

    private void loadScoreboard() {
        try {
            List<User> topUsers = DatabaseConnection.getTopUsers(10); // Get top 10 users
            scoreboardListView.getItems().clear();
            for (User user : topUsers) {
                scoreboardListView.getItems().add(user.getNickname() + ": " + user.getHighScore());
            }
        } catch (SQLException e) {
            Tools.showAlert("Error", "Database Error", "An error occurred while fetching the scoreboard. Please try again.");
            e.printStackTrace();
        }
    }


    @FXML
    private void goToMainMenu() {
        App.loadScene(Menu.MAIN_MENU.getPath());
    }
}
