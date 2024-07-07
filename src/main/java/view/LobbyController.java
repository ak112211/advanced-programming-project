package view;

import enums.Menu;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import model.App;
import model.Game;
import model.User;
import util.DatabaseConnection;
import util.ServerConnection;

import java.sql.SQLException;
import java.util.Objects;

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
        App.loadScene(Menu.DECK_MENU.getPath());
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
                    DatabaseConnection.saveGame(game);
                    new GameLauncher().start(App.getStage());
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
