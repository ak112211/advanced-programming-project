package enums;

public enum Row {
    WEATHER,
    PLAYER1_CLOSE_COMBAT,
    PLAYER2_CLOSE_COMBAT,
    PLAYER1_RANGED,
    PLAYER2_RANGED,
    PLAYER1_SIEGE,
    PLAYER2_SIEGE;
    public static boolean isPlayer1(Row row) {
        return row.equals(Row.PLAYER1_SIEGE) ||
                row.equals(Row.PLAYER1_RANGED) ||
                row.equals(Row.PLAYER1_CLOSE_COMBAT);
    }
    public static boolean isPlayer2(Row row) {
        return row.equals(Row.PLAYER2_SIEGE) ||
                row.equals(Row.PLAYER2_RANGED) ||
                row.equals(Row.PLAYER2_CLOSE_COMBAT);
    }
}
