package view;

import controller.AppController;
import enums.Menu;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.App;
import model.User;
import util.DatabaseConnection;
import util.ServerConnection;

import java.sql.SQLException;
import java.util.List;

public class ScoreboardController implements ServerConnection.ServerEventListener {

    @FXML
    private ListView<String> scoreboardListView;

    @FXML
    private void initialize() {
        scoreboardListView.getItems().clear();
        loadScoreboard();
        App.getServerConnection().addMessageListener(this);
    }

    private void loadScoreboard() {
        try {
            List<User> topUsers = DatabaseConnection.getTopUsers(100); // Get top 10 users
            for (User user : topUsers) {
                App.getServerConnection().sendMessage("check status:" + user.getUsername());
            }
        } catch (SQLException e) {
            Tools.showAlert("Error", "Database Error", "An error occurred while fetching the scoreboard. Please try again.");
            e.printStackTrace();
        }
    }

    @Override
    public void handleServerEvent(String input) {
        Platform.runLater(() -> {
            if (input.endsWith("is online")) {
                try {
                    User user = DatabaseConnection.getUser(input.split(" ")[0]);
                    assert user != null;
                    scoreboardListView.getItems().add(user.getNickname() + " score: " + user.getHighScore() + " status: online");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (input.endsWith("is offline")) {
                User user = null;
                try {
                    user = DatabaseConnection.getUser(input.split(" ")[0]);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                assert user != null;
                scoreboardListView.getItems().add(user.getNickname() + " score: " + user.getHighScore() + " status: offline");
            } else if (input.startsWith("update scoreboard")) {
                scoreboardListView.getItems().clear();
                loadScoreboard();
            }
        });
    }

    @FXML
    private void goToMainMenu() {
        AppController.loadScene(Menu.MAIN_MENU);
    }

    public void cleanup() {
        App.getServerConnection().removeMessageListener(this);
    }

}
