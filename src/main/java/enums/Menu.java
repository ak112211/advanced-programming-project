package enums;

public enum Menu {
    LOGIN_MENU("/fxml/LoginMenu.fxml"),
    SCORE_MENU("/fxml/ScoreboardMenu.fxml"),
    REGISTER_MENU("/fxml/RegisterMenu.fxml"),
    FORGET_PASSWORD_MENU("/fxml/ForgotPasswordMenu.fxml"),
    MAIN_MENU("/fxml/MainMenu.fxml"),
    PROFILE_MENU("/fxml/ProfileMenu.fxml"),
    GAME_PANE("/fxml/GamePane.fxml"),
    CHAT_MENU("/fxml/ChatMenu.fxml"),
    DECK_MENU("/fxml/ChooseDeckMenu.fxml"),
    VERIFY_MENU("/fxml/VerificationCodeMenu.fxml"),
    MESSAGING_MENU("/fxml/MessagingMenu.fxml"),
    VIEW_GAMEPLAY_MENU("/fxml/ViewGamePlay.fxml"),
    ONGOING_GAMES_MENU("/fxml/OngoingGames.fxml"),
    LOBBY_MENU("/fxml/LobbyMenu.fxml"),
    CHOOSE_NAME_MENU("/fxml/ChooseName.fxml"),
    LEAGUE_MENU("/fxml/LeagueMenu.fxml"),
    CHOOSE_DECK_MENU("/fxml/ChooseDeckMenu.fxml"),
    ;

    private final String path;

    Menu(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
