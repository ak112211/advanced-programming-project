package controller;

import enums.Menu;
import model.App;
import model.Command;
import view.MainMenu;

public class MainController extends AppController {

    @Override
    public String menuEnter(String menuName) {
        return null;
    }

    @Command(command = "user logout")
    public void logout() {
        App.setMenu(Menu.LoginMenu);
        App.setController(new LoginController());
    }

}
