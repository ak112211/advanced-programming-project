package controller;

import model.Command;

public class RegisterController extends AppController {
    @Command(command = "register -u <username> -p <password> -c <passwordConfirm> -n <nickname> -e <email>")
    public String register(String username, String password, String passwordConfirm, String nickname, String email) {
        return null;
    }

    @Command(command = "pick question -q <questionNumber> -a <answer> -c <answer_confirm>")
    public String pickQuestion() {
        return null;
    }

}
