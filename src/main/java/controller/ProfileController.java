package controller;

import model.Command;
import view.MainMenu;
import view.ProfileMenu;

import java.util.HashMap;

public class ProfileController extends AppController {

    @Override
    public String menuEnter(String menuName) {
        return null;
    }

    @Command(command = "change username -u <username>")
    public String changeUsername(String username) {
        // Implementation to change the username
        return null;
    }

    @Command(command = "change nickname -u <nickname>")
    public String changeNickname(String nickname) {
        // Implementation to change the nickname
        return null;
    }

    @Command(command = "change email -e <email>")
    public String changeEmail(String email) {
        // Implementation to change the email
        return null;
    }

    @Command(command = "change password -p <newPassword> -o <oldPassword>")
    public String changePassword(String newPassword, String oldPassword) {
        // Implementation to change the password
        return null;
    }

    @Command(command = "game history -n <n>")
    public String displayGameHistory(int n) {
        // Implementation to display game history
        // Code to fetch and display game history from the database or any storage
        return null;
    }
}


