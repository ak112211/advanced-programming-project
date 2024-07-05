package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import model.App;
import util.ServerConnection;

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
