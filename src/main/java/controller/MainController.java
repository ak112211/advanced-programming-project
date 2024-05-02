package controller;

import enums.Menu;
import model.App;
import model.Command;

public class MainController extends AppController {

    @Command(command = "user logout")
    public String logout() {
        App.setMenu(Menu.LoginMenu);
        App.setController(new LoginController());
        return "logout";
    }
}
