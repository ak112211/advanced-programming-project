package enums;

public enum Menu {
    LOGIN_MENU("/fxml/LoginMenu.fxml"),
    REGISTER_MENU("/fxml/RegisterMenu.fxml"),
    FORGET_PASSWORD_MENU("/fxml/ForgetPasswordMenu.fxml"),
    MAIN_MENU("/fxml/MainMenu.fxml"),
    PROFILE_MENU("/fxml/ProfileMenu.fxml"),
    ADD_FREINDS_MENU("/fxml/AddFriendsMenu.fxml"),
    ;

    private final String PATH;

    Menu(String path) {
        this.PATH = path;
    }

    public String getPath() {
        return PATH;
    }
}
