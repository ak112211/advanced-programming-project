package controller;

import model.Command;

public class LoginController extends AppController {
    @Command(command = "login -u <username> -p <password> --stayLoggedIn")
    public String login(String username, String password, boolean stayLoggedIn) { // It works!!!
        return "username : " + username + ", password : " + password + ", stayLoggedIn : " + stayLoggedIn;
    }
}
