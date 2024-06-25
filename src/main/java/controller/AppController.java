package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import util.ServerConnection;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class AppController {
    private static Stage stage;
    private static ServerConnection serverConnection;
    public static void setStage (Stage stage) {
        AppController.stage = stage;
    }

    public static Stage getStage () {
        return stage;
    }

    public static void loadScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(AppController.class.getResource(fxmlPath)));
            Stage stage = AppController.getStage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setServerConnection() {
        serverConnection = new ServerConnection();
    }

    public static ServerConnection getServerConnection() {
        return serverConnection;
    }
}