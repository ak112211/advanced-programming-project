package controller;

import model.Command;

public class RegisterController extends AppController {
    @Command(command = "register -u <username> -p <password> -c <passwordConfirm> -n <nickname> -e <email>")
    public String register(String username, String password, String passwordConfirm, String nickname, String email) {
        return null;
    }

    @Command(command = "pick question -q <questionNumber> -a <answer> -c <answerConfirm>")
    public String pickQuestion() {
        return null;
    }

    public String showQuestions() {
        return null;
    }

    public static boolean isUsernameFormatValid(String username) {
        return false;
    }

    public static boolean isPasswordFormatValid(String password) {
        return false;
    }

    public static String passwordStrengthChecker(String password) {
        return null;
    }

    public static boolean isValidNickname(String nickname) {
        return false;
    }

    public static boolean isEmailFormatValid(String email) {
        return false;
    }


}
