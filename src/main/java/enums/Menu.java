package enums;

public enum Menu {
    LOGIN_MENU("/fxml/LoginMenu.fxml"),
    SCORE_MENU("/fxml/Scoreboard.fxml"),
    REGISTER_MENU("/fxml/RegisterMenu.fxml"),
    FORGET_PASSWORD_MENU("/fxml/ForgotPasswordMenu.fxml"),
    MAIN_MENU("/fxml/MainMenu.fxml"),
    PROFILE_MENU("/fxml/ProfileMenu.fxml"),
    GAME_PANE("/fxml/GamePane.fxml"),
    CHAT_MENU("/fxml/ChatMenu.fxml"),
    DECK_MENU("/fxml/ChooseDeckMenu.fxml"),
    VERIFY_MENU("/fxml/VerificationCode.fxml"),
    MESSAGING_MENU("/fxml/MessagingMenu.fxml")
    ;

    private final String path;

    Menu(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
