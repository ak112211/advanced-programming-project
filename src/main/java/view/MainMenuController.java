package view;

import enums.Menu;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import model.App;
import model.Game;
import model.User;
import util.DatabaseConnection;
import util.ServerConnection;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

import static view.Tools.clearUserSession;

public class MainMenuController implements ServerConnection.ServerEventListener {
    @FXML
    public Label usernameField;
    @FXML
    public ImageView backgroundImageView;
    @FXML
    public ListView<String> savedGamesListView;

    public List<Game> savedGames;

    @FXML
    public void initialize() {
        usernameField.setText("Hi " + User.getCurrentUser().getUsername() + "!");
        backgroundImageView.setImage(Tools.getImage("/gwentImages/img/maxresdefault.jpg"));
        loadSavedGames();
        App.getServerConnection().addMessageListener(this);

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
        Tools.loadScene(Menu.PROFILE_MENU);
    }

    @FXML
    private void goToAddFriends() {
        Tools.loadScene(Menu.CHAT_MENU);
    }

    @FXML
    private void showScoreboard() {
        Tools.loadScene(Menu.SCORE_MENU);
    }

    @FXML
    private void goToDeckMenu() {
        ChooseDeckMenuController.isMulti = false;
        Tools.loadScene(Menu.CHOOSE_NAME_MENU);
    }

    @FXML
    private void logout() {
        App.getServerConnection().sendMessage("logout");
        User.setCurrentUser(null);
        Game.setCurrentGame(null);
        clearUserSession();
        Tools.loadScene(Menu.LOGIN_MENU);
    }

    @Override
    public void handleServerEvent(String input) {
        Platform.runLater(() -> {
            if (input.startsWith("Friend request from ")
                    || input.startsWith("Game request from ")
                    || input.startsWith("Message from ")
                    || input.startsWith("Game request accepted by ")
                    || input.startsWith("Friend request accepted by ")) {
                if (!App.isIsGameIn()) {
                    showAlert(input);
                }
                if (input.startsWith("Game request accepted by ")) {
                    try {
                        ChooseDeckMenuController.player2 = DatabaseConnection.getUser(input.split(" ")[4]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    startGame();
                }
            }
        });
    }

    private void showAlert(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message, "Message from Server", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void startGame() {
        ChooseDeckMenuController.isMulti = true;
        Tools.loadScene(Menu.CHOOSE_NAME_MENU);
    }

    public void cleanup() {
        App.getServerConnection().removeMessageListener(this);
    }

    @FXML
    public void goToOnGoingGames(ActionEvent actionEvent) {
        Tools.loadScene(Menu.ONGOING_GAMES_MENU);
    }
}
