package enums.cardsinformation;

public enum Description {
    NOTHING(null),
    KILLS_UNITS("Kills the opponent's card(s) with most power in enemy's Ranged combat row if the sum of powers of none-hero cards in this row is 10 or more."),
    SETS_CLOSE_COMBAT_TO_ONE("Sets the power of all close combat units of both sides to 1"),
    SETS_RANGED_TO_ONE("Sets the power of all ranged units of both sides to 1"),
    DOUBLES_POWER_IN_ROW("Doubles the power of the cards in the row that was placed. Only one of this type can be played in a row"),
    DECOY_CARD("This feature applies only to the Decoy card itself, and allows a card from your own page to be played. The card on which the game is played will be added to the playable cards pile."),
    BECOMES_STRONGER_NEXT_ROUND("Turns into a card with a power of 8 after one round"),
    SCOTCH_ON_ENEMY_CLOSE_COMBAT("Works only on opponent’s close combat"),
    KILLS_STRONGEST_ENEMY("kills the opponent's card(s) with most power in enemy's Siege combat row if the sum of powers of none-hero cards in this row is 10 or more"),
    Mardroeme("This Spell card has Mardoeme Ability and can be placed in the commmander's Horn spot . it effects the row that it was placed in"),
    TRANSFORMS_TO_VIDKAARL("Transforms into Vidkaarl when Mardroeme is used.Vidkaarl has Morale Boost effect."),
    PLAYED_WITH_BERSERKER("it can only be played when a berserker transform into it."),
    SUMMONS_SHIELD_MAIDEN("Its Muster effect will summon Shield Maiden cards and other musters."),
    TURNS_INTO_STRONGER_CARD("Turns into a card with a power of 8 after one round"),
    MUSTERED_BY_CERYS("Can be Mustered by Cerys."),
    KILLS_STRONGEST_OPPONENT("kills opponent card(s) with most power(does not matter in which row in this card)"),
    TRANSFORMS_TO_YOUNG_VIDKAARL("Transforms into Young Vidkaarl when Mardroeme is used.Young Vidkaarl has Tight Bond effect."),
    PLAYED_WITH_YOUNG_BERSERKER("it can only be played when a young berserker transform into it.\n");
    private final String description;

    Description(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}