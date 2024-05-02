package controller;

import model.Command;
import view.MainMenu;
import view.ProfileMenu;

import java.util.HashMap;

public class ProfileController extends AppController {

    @Command(command = "show current menu")
    public void showCurrentMenu() {
        ProfileMenu.showCurrentMenu();
    }

    @Command(command = "change username -u <username>")
    public void changeUsername(String username) {
        // Implementation to change the username
    }

    @Command(command = "change nickname -u <nickname>")
    public void changeNickname(String nickname) {
        // Implementation to change the nickname
    }

    @Command(command = "change email -e <email>")
    public void changeEmail(String email) {
        // Implementation to change the email
    }

    @Command(command = "change password -p <new_password> -o <old_password>")
    public void changePassword(String newPassword, String oldPassword) {
        // Implementation to change the password
    }

    @Command(command = "menu enter user info")
    public void displayUserInfo() {
        // Implementation to display user information
        // Code to fetch and display user info from the database or any storage
    }

    @Command(command = "game history -n <n>")
    public void displayGameHistory(int n) {
        // Implementation to display game history
        // Code to fetch and display game history from the database or any storage
    }
}


