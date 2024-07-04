package view;

import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.App;
import model.Game;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static view.Tools.clearUserSession;

public class MainMenuController {
    @FXML
    private Label usernameField;
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private ListView<String> savedGamesListView;

    private List<Game> savedGames;

    @FXML
    private void initialize() {
        usernameField.setText("Hi " + User.getCurrentUser().getUsername() + "!");
        backgroundImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gwentImages/img/maxresdefault.jpg"))));
        loadSavedGames();
    }

    private void loadSavedGames() {
        try {
            savedGames = DatabaseConnection.getSavedOfflineGames(User.getCurrentUser().getUsername());
            for (Game game : savedGames) {
                savedGamesListView.getItems().add("Game ID: " + game.getID() + " Date: " + game.getDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleContinueGameButtonAction() {
        String selectedGame = savedGamesListView.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            int selectedGameId = Integer.parseInt(selectedGame.split(" ")[2]);
            Game.setCurrentGame(savedGames.stream().filter(game -> game.getID() == selectedGameId).findFirst().orElse(null));
            new GameLauncher().start(App.getStage());
        }
    }

    @FXML
    private void goToProfile() {
        App.loadScene(Menu.PROFILE_MENU.getPath());
    }

    @FXML
    private void goToAddFriends() {
        App.loadScene(Menu.CHAT_MENU.getPath());
    }

    @FXML
    private void showScoreboard() {
        App.loadScene(Menu.SCORE_MENU.getPath());
    }

    @FXML
    private void goToDeckMenu() {
        ChooseDeckMenuController.isMulti = false;
        App.loadScene(Menu.DECK_MENU.getPath());
    }

    @FXML
    private void logout() {
        App.getServerConnection().sendMessage("logout");
        User.setCurrentUser(null);
        Game.setCurrentGame(null);
        clearUserSession();
        App.loadScene(Menu.LOGIN_MENU.getPath());
    }
}
