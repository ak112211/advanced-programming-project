package enums;

import view.*;

import java.util.Scanner;

public enum Menu {
    LoginMenu(new LoginMenu()),
    RegisterMenu(new RegisterMenu()),
    MainMenu(new MainMenu()),
    ProfileMenu(new ProfileMenu()),
    GameMenu(new GameMenu());

    private final AppMenu MENU;

    Menu(AppMenu menu) {
        this.MENU = menu;
    }

    public void checkCommand(Scanner scanner) {
        this.MENU.check(scanner);
    }

}
