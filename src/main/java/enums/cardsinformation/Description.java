package enums.cardsinformation;

public enum Description {
    NOTHING(null),
    KILLS_UNITS("Kills the opponent's card(s) with most power in enemy's Ranged combat row if the sum of powers of none-hero cards in this row is 10 or more."),
    SETS_CLOSE_COMBAT_TO_ONE("Sets the power of all close combat units of both sides to 1"),
    SETS_RANGED_TO_ONE("Sets the power of all ranged units of both sides to 1"),
    DOUBLES_POWER_IN_ROW("Doubles the power of the cards in the row that was placed. Only one of this type can be played in a row"),
    DECOY_CARD("This feature applies only to the Decoy card itself, and allows a card from your own page to be played. The card on which the game is played will be added to the playable cards pile."),
    BECOMES_STRONGER_NEXT_ROUND("Turns into a card with a power of 8 after one round"),
    SCOTCH_ON_ENEMY_CLOSE_COMBAT("Works only on opponent’s close combat");
    private final String description;

    Description(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}