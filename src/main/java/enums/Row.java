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
    public static boolean isPlayer1(Row row) {
        return row.equals(Row.PLAYER1_SIEGE) ||
                row.equals(Row.PLAYER1_RANGED) ||
                row.equals(Row.PLAYER1_CLOSE_COMBAT) ||
                row.equals(Row.PLAYER1_WEATHER);
    }
    public static boolean isSiege(Row row) {
        return row.equals(Row.PLAYER1_SIEGE) ||
                row.equals(Row.PLAYER2_SIEGE);
    }
    public static boolean isRanged(Row row) {
        return row.equals(Row.PLAYER1_RANGED) ||
                row.equals(Row.PLAYER2_RANGED);
    }
    public static boolean isCloseCombat(Row row) {
        return row.equals(Row.PLAYER1_CLOSE_COMBAT) ||
                row.equals(Row.PLAYER2_CLOSE_COMBAT);
    }
    public static boolean isWeather(Row row) {
        return row.equals(Row.PLAYER1_WEATHER) ||
                row.equals(Row.PLAYER2_WEATHER);
    }
}
