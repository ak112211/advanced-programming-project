package view;

import controller.AppController;
import enums.Menu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import model.App;
import model.Game;
import util.DatabaseConnection;

import java.sql.SQLException;
import java.util.List;

public class OngoingGamesController {

    @FXML
    private ListView<String> ongoingGamesListView;
    @FXML
    private Button viewGameButton;

    private List<Game> activeGames;

    @FXML
    private void initialize() {
        loadActiveGames();
    }

    private void loadActiveGames() {
        try {
            activeGames = DatabaseConnection.getActiveGames();
            for (Game game : activeGames) {
                ongoingGamesListView.getItems().add("Game ID: " + game.getID() + " | Players: " + game.getPlayer1().getUsername() + " vs " + game.getPlayer2().getUsername());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewSelectedGame(ActionEvent event) {
        String selectedGameInfo = ongoingGamesListView.getSelectionModel().getSelectedItem();
        if (selectedGameInfo != null) {
            // Extract game ID from the selected item
            int gameId = Integer.parseInt(selectedGameInfo.split(" ")[2]);

            // Find the selected game object from the list of active games
            Game selectedGame = null;
            for (Game game : activeGames) {
                if (game.getID() == gameId) {
                    selectedGame = game;
                    break;
                }
            }

            if (selectedGame != null) {
                // Load the view gameplay screen and pass the selected game
                ViewGamePlayController.game = selectedGame;
                AppController.loadScene(Menu.VIEW_GAMEPLAY_MENU);
            }
        }
    }

    @FXML
    private void handleBack() {
        AppController.loadScene(Menu.MAIN_MENU);
    }
}
