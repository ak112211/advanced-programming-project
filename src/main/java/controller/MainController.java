package controller;

import enums.Menu;
import model.App;
import model.Command;
import view.MainMenu;

public class MainController extends AppController {

    @Command(command = "user logout")
    public void logout() {
        App.setMenu(Menu.LoginMenu);
        App.setController(new LoginController());
    }

    @Command(command = "show current menu")
    public void showCurrentMenu() {
        MainMenu.showCurrentMenu();
    }

    @Command(command = "menu exit")
    public void exitMenu() {

    }

}
