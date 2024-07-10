package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import model.App;
import model.Game;
import model.RoundsInfo;
import model.User;
import util.DatabaseConnection;
import util.ServerConnection;

import java.sql.SQLException;

import static util.DatabaseConnection.updateUserScore;
import static view.Tools.loadUserSession;

public class MainApp extends Application {
    public MediaPlayer mediaPlayer; // Assuming this handles your background music
    boolean isMute;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Tools.clearUserSession();
        stage.setResizable(false);
        stage.centerOnScreen();
        App.setStage(stage);
        App.setServerConnection(new ServerConnection());

        stage.getIcons().add(Tools.getImage("/gwentImages/img/icon.png"));

        FXMLLoader fxmlLoader = new FXMLLoader();
        App.setCurrentController(fxmlLoader.getController());

        stage.setTitle("Gwent");

        loadUserSession();
        setupBackgroundMusic();

        App.getStage().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SHIFT) {
                handleToggleSound();
            }
        });
    }

    @Override
    public void stop() {
        // Perform cleanup tasks here
        if (App.getServerConnection() != null) {
            if (Game.getCurrentGame() != null && Game.getCurrentGame().isOnline()) {
                Game game = Game.getCurrentGame();
                if (game.getPlayer1().getUsername().equals(User.getCurrentUser().getUsername())) {
                    game.getRoundsInfo().setWinner(RoundsInfo.Winner.PLAYER2);
                } else {
                    game.getRoundsInfo().setWinner(RoundsInfo.Winner.PLAYER1);
                }

                game.setStatus(Game.GameStatus.COMPLETED);
                game.getPlayer1().setHighScore(game.getRoundsInfo().getPlayer1TotalScore() + game.getPlayer1().getHighScore());
                game.getPlayer2().setHighScore(game.getRoundsInfo().getPlayer2TotalScore() + game.getPlayer2().getHighScore());
                try {
                    DatabaseConnection.updateGame(game);
                    updateUserScore(game.getPlayer1());
                    updateUserScore(game.getPlayer2());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                App.getServerConnection().sendMessage(game.getWinnerUser().getUsername() + ":ended game");
            }
            App.getServerConnection().sendMessage("logout");
            App.getServerConnection().close();
        }
        Platform.exit();
        System.exit(0);
    }

    private void setupBackgroundMusic() {
        mediaPlayer = Tools.getMediaPlayer("/media/Ramin-Djawadi-Finale-128.mp3");
        mediaPlayer.play();
    }

    public void handleToggleSound() {
        if (isMute) {
            isMute = false;
            mediaPlayer.play();
        } else {
            isMute = true;
            mediaPlayer.stop();
        }
    }
}
