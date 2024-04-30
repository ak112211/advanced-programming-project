package controller;

import model.App;
import model.Command;

public abstract class AppController {
    public String a = "A";
    @Command(command = "show current menu")
    public String getCurrentMenu() {
        return App.getMenu().name();
    }
    @Command(command = "show a")
    public String getA() {
        return a;
    }
}
