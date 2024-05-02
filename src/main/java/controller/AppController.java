package controller;

import model.App;
import model.Command;

public abstract class AppController {

    @Command(command = "show current menu")
    public String getCurrentMenu() {
        return App.getMenu().name();
    }

    @Command(command = "exit")
    public String exit() {
        System.exit(0);
        return null;
    }
}
