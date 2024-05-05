package enums;

public enum Row {
    PLAYER1_WEATHER,
    PLAYER2_WEATHER,
    PLAYER1_CLOSE_COMBAT,
    PLAYER2_CLOSE_COMBAT,
    PLAYER1_RANGED,
    PLAYER2_RANGED,
    PLAYER1_SIEGE,
    PLAYER2_SIEGE;
    public boolean isPlayer1() {
        return this == Row.PLAYER1_SIEGE ||
                this == Row.PLAYER1_RANGED ||
                this == Row.PLAYER1_CLOSE_COMBAT ||
                this == Row.PLAYER1_WEATHER;
    }
    public boolean isSiege() {
        return this == Row.PLAYER1_SIEGE ||
                this == Row.PLAYER2_SIEGE;
    }
    public boolean isRanged() {
        return this == Row.PLAYER1_RANGED ||
                this == Row.PLAYER2_RANGED;
    }
    public boolean isCloseCombat() {
        return this == Row.PLAYER1_CLOSE_COMBAT ||
                this == Row.PLAYER2_CLOSE_COMBAT;
    }
    public boolean isWeather() {
        return this == Row.PLAYER1_WEATHER ||
                this == Row.PLAYER2_WEATHER;
    }
}
