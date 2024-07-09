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
    public static User user;
    public static Stage stage;
    public static Menu menu;
    public static Stage messagingStage;
    public static String menuPath;
    public static boolean isGameIn;
    public static ServerConnection serverConnection;

    public static void setStage(Stage stage) {
        App.stage = stage;
    }

    public static Stage getStage() {
        return stage;
    }
    public static Object currentController;

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

    public static String getMenuPath() {
        return menuPath;
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
