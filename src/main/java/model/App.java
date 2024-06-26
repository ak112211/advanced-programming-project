package model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.ServerConnection;

import java.io.IOException;
import java.util.Objects;

public class App {
    private static User user;
    private static Stage stage;
    private static ServerConnection serverConnection;

    public static void setStage(Stage stage) {
        App.stage = stage;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void loadScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource(fxmlPath)));
            Stage stage = App.getStage();
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

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        App.user = user;
    }
}
