package controller;

import model.Command;

public class LoginController extends AppController {
    @Command(command = "login")
    public String login() {
        return "salam";
    }
}
