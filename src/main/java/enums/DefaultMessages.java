package enums;

public enum DefaultMessages {
    HELLO("hello"),
    HI("hi"),
    GOOD_BYE("good bye"),
    GG("GG"),
    GG_EZ("GG EZ"),
    GOOD_GAME("good game"),
    NICE("nice"),
    NEWBIE("newbie!"),
    F("F***"),
    LUCKY("lucky"),
    DAMN_IT("damn it"),
    ;


    private final String message;

    DefaultMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
