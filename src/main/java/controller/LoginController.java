package controller;

import enums.Menu;
import model.App;
import model.Command;

public class LoginController extends AppController {
    @Command(command = "login -u <username> -p <password> --stayLoggedIn")
    public String login(String username, String password, boolean stayLoggedIn) {
        App.setMenu(Menu.MainMenu);
        App.setController(new MainController());
        return "username : " + username + ", password : " + password + ", stayLoggedIn : " + stayLoggedIn;
    }

    private static String getRandomPassword() {
        return null;
    }
}
