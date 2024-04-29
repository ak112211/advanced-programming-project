package controller;

import model.App;
import model.Command;

public abstract class AppController {
    @Command(command = "show current menu")
    public String getCurrentMenu() {
        return App.getMenu().name();
    }
}
