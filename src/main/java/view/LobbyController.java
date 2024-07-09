package view;

import controller.AppController;
import enums.Menu;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import model.App;
import model.Game;
import model.League;
import model.User;
import util.DatabaseConnection;
import util.ServerConnection;

import java.sql.SQLException;

public class LobbyController implements ServerConnection.ServerEventListener {

    @FXML
    private Label waitingLabel;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private void initialize() {
        App.getServerConnection().addMessageListener(this);
    }

    @FXML
    private void handleCancel() {
        AppController.loadScene(Menu.DECK_MENU.getPath());
    }

    public void setWaitingMessage(String message) {
        waitingLabel.setText(message);
    }

    public void setProgressIndicatorVisibility(boolean visible) {
        progressIndicator.setVisible(visible);
    }

    @Override
    public void handleServerEvent(String input) {
        Platform.runLater(() -> {
            if (input.endsWith("loaded deck new")) {
                try {
                    User currentUser = User.getCurrentUser();
                    User player2 = DatabaseConnection.getUser(input.split(" ")[0]);
                    if (currentUser.getDeck() == null || (player2.getDeck() == null) || currentUser.getDeck().getCards().size() < 22 || (player2.getDeck().getCards().size() < 22)) {
                        Tools.showAlert("Error", "Deck Error", "Both players must have at least 22 unit cards to start the game.");
                        return;
                    }

                    Game game = new Game(currentUser, player2);
                    game.setCurrentUser(User.getCurrentUser());
                    game.setOnline(true);
                    Game.setCurrentGame(game);
                    if (ChooseDeckMenuController.league != null) {
                        League league = ChooseDeckMenuController.league;
                        switch (ChooseDeckMenuController.leagueGameStep) {
                            case "q1" -> league.setQuarter1Game(String.valueOf(game.getID()));
                            case "q2" -> league.setQuarter2Game(String.valueOf(game.getID()));
                            case "q3" -> league.setQuarter3Game(String.valueOf(game.getID()));
                            case "q4" -> league.setQuarter4Game(String.valueOf(game.getID()));
                            case "s1" -> league.setSemi1Game(String.valueOf(game.getID()));
                            case "s2" -> league.setSemi2Game(String.valueOf(game.getID()));
                            case "f" -> league.setFinalPlay(String.valueOf(game.getID()));
                        }
                        DatabaseConnection.updateLeague(league);
                        game.setPublic(true);
                    }
                    DatabaseConnection.saveGame(game);
                    new GameLauncher().start(App.getStage());
                    Tools.openMessagingWindow(Game.getCurrentGame().getPlayer2().getUsername());

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (input.contains("loaded deck after with id: ")) {
                try {
                    Game game = DatabaseConnection.getGame(Integer.parseInt(input.split(" ")[6]));
                    Game.setCurrentGame(game);
                    assert game != null;
                    game.setOnline(true);
                    new GameLauncher().start(App.getStage());
                    Tools.openMessagingWindow(Game.getCurrentGame().getPlayer1().getUsername());

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void cleanup() {
        App.getServerConnection().removeMessageListener(this);
    }
}
