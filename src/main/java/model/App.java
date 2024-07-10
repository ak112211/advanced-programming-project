package model;

import enums.Menu;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.ServerConnection;
import view.*;

import java.io.IOException;
import java.util.Objects;

public class App {
    private static User user;
    private static Stage stage;
    private static Menu menu;
    private static Stage messagingStage;
    private static boolean isGameIn;
    private static ServerConnection serverConnection;

    public static void setStage(Stage stage) {
        App.stage = stage;
    }

    public static Stage getStage() {
        return stage;
    }

    private static Object currentController;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        App.user = user;
    }

    public static Menu getMenu() {
        return menu;
    }

    public static void setMenu(Menu menu) {
        App.menu = menu;
    }

    public static Stage getMessagingStage() {
        return messagingStage;
    }

    public static void setMessagingStage(Stage messagingStage) {
        App.messagingStage = messagingStage;
    }

    public static ServerConnection getServerConnection() {
        return serverConnection;
    }

    public static void setServerConnection(ServerConnection serverConnection) {
        App.serverConnection = serverConnection;
    }

    public static boolean isIsGameIn() {
        return isGameIn;
    }

    public static void setIsGameIn(boolean isGameIn) {
        App.isGameIn = isGameIn;
    }

    public static Object getCurrentController() {
        return currentController;
    }

    public static void setCurrentController(Object currentController) {
        App.currentController = currentController;
    }
}
