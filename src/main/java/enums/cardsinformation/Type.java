package enums.cardsinformation;

import enums.Row;

public enum Type {
    SPELL,
    CLOSE_COMBAT_UNIT,
    RANGED_UNIT,
    SIEGE_UNIT,
    WEATHER,
    AGILE_UNIT,
    SPY_UNIT;
    public Row getRow(boolean isPlayer1){
        if (isPlayer1){
            return switch (this) {
                case CLOSE_COMBAT_UNIT -> Row.PLAYER1_CLOSE_COMBAT;
                case RANGED_UNIT -> Row.PLAYER1_RANGED;
                case SIEGE_UNIT -> Row.PLAYER1_SIEGE;
                case WEATHER -> Row.PLAYER1_WEATHER;
                case SPY_UNIT -> Row.PLAYER2_CLOSE_COMBAT;
                case SPELL, AGILE_UNIT -> Row.PLAYER1_CLOSE_COMBAT; // yejoorie vali khob
            };
        } else {
            return switch (this) {
                case CLOSE_COMBAT_UNIT -> Row.PLAYER2_CLOSE_COMBAT;
                case RANGED_UNIT -> Row.PLAYER2_RANGED;
                case SIEGE_UNIT -> Row.PLAYER2_SIEGE;
                case WEATHER -> Row.PLAYER2_WEATHER;
                case SPY_UNIT -> Row.PLAYER1_CLOSE_COMBAT;
                case SPELL, AGILE_UNIT -> Row.PLAYER2_CLOSE_COMBAT; // yejoorie vali khob
            };
        }
    }
}

