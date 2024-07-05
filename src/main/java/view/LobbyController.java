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

import java.io.IOException;
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
        // Handle cancel action, such as going back to the main menu
        System.out.println("Cancel button clicked");
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
                    Game game = new Game(User.getCurrentUser(), DatabaseConnection.getUser(input.split(" ")[0]));
                    game.setCurrentPlayer(User.getCurrentUser());
                    game.setOnline(true);
                    Game.setCurrentGame(game);
                    DatabaseConnection.saveGame(Game.getCurrentGame());
                    App.getServerConnection().sendMessage(DatabaseConnection.getUser(input.split(" ")[0]) + ":loaded after:" + game.getID());
                    new GameLauncher().start(App.getStage());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (input.contains("loaded deck after with id: ")) {
                try {
                    Game game = DatabaseConnection.getGame(Integer.parseInt(input.split(" ")[6]));
                    Game.setCurrentGame(game);
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
