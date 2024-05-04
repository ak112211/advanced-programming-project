package controller;

import model.Command;

public class LoginController extends AppController {

    @Override
    public String menuEnter(String menuName) {
        return null;
    }

    @Command(command = "login -u <username> -p <password> --stayLoggedIn")
    public String login(String username, String password, boolean stayLoggedIn) {
        return null;
    }

    @Command(command = "forget password -u <username>")
    public String forgetPassword(String username) {
        return null;
    }

    @Command(command = "answer -q <questionNumber> -a <answer>")
    public String answer(int questionNumber, String answer) {
        return null;
    }

    @Command(command = "set password -p <password>")
    public String setPassword(String password) {
        return null;
    }

    private static String getRandomPassword() {
        return null;
    }

}
