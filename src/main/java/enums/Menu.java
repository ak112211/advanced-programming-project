package enums;

public enum Menu {
    LOGIN_MENU("/fxml/LoginMenu.fxml"),
    REGISTER_MENU("/fxml/RegisterMenu.fxml"),
    FORGET_PASSWORD_MENU("/fxml/ForgotPasswordMenu.fxml"),
    MAIN_MENU("/fxml/MainMenu.fxml"),
    PROFILE_MENU("/fxml/ProfileMenu.fxml"),
    CHAT_MENU("/fxml/ChatMenu.fxml"),
    DECK_MENU("/fxml/ChooseDeckMenu.fxml"),
    ;

    private final String PATH;

    Menu(String path) {
        this.PATH = path;
    }

    public String getPath() {
        return PATH;
    }
}
