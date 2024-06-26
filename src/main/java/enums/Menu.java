package enums;

public enum Menu {
    LOGIN_MENU("/fxml/LoginMenu.fxml"),
    REGISTER_MENU("/fxml/RegisterMenu.fxml"),
    FORGET_PASSWORD_MENU("/fxml/ForgetPasswordMenu.fxml"),
    MAIN_MENU("/fxml/MainMenu.fxml"),
    PROFILE_MENU("/fxml/ProfileMenu.fxml"),
    AVATAR_MENU("/fxml/AvatarMenu.fxml"),
    RANKING_MENU("/fxml/RankingMenu.fxml"),
    SETTINGS_MENU("/fxml/SettingsMenu.fxml"),
    KEYBOARD_MENU("/fxml/KeyboardMenu.fxml"),
    PAUSE_MENU("/fxml/PauseMenu.fxml"),
    ;

    private final String PATH;

    Menu(String path) {
        this.PATH = path;
    }

    public String getPath() {
        return PATH;
    }
}
