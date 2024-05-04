package model;

import controller.AppController;
import controller.LoginController;
import enums.Menu;
import view.ProfileMenu;

public class App {

    private static Menu menu = Menu.LoginMenu;
    private static AppController controller = new LoginController();
    private static User user;
    private static Game game;

    public static Menu getMenu() {
        return menu;
    }

    public static void setMenu(Menu menu) {
        App.menu = menu;
    }

    public static AppController getController() {
        return controller;
    }

    public static void setController(AppController controller) {
        App.controller = controller;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        App.user = user;
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        App.game = game;
    }
}
