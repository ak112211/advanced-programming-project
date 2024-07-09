package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.App;
import view.*;

import java.io.IOException;
import java.util.Objects;

import static model.App.currentController;

public class AppController {
    public static void loadScene(String fxmlPath) {
        try {
            if (currentController != null) {
                switch (currentController) {
                    case ChatController chatController -> chatController.cleanup();
                    case LobbyController lobbyController -> lobbyController.cleanup();
                    case GamePaneController gamePaneController -> gamePaneController.cleanup();
                    case MainMenuController mainMenuController -> mainMenuController.cleanup();
                    case MessagingController messagingController -> messagingController.cleanup();
                    case ScoreboardController scoreboardController -> scoreboardController.cleanup();
                    case ViewGamePlayController viewGamePlayController -> viewGamePlayController.cleanup();
                    default -> {
                    }
                }
            }
            FXMLLoader loader = new FXMLLoader(Tools.class.getResource(fxmlPath));
            Parent root = loader.load();
            currentController = loader.getController();
            Stage stage = App.getStage();
            App.menuPath = fxmlPath;
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(App.class.getResource("/css/styles.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
