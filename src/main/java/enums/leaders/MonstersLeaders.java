package enums.leaders;

import enums.cardsinformation.Faction;
import model.card.Leader;

public enum MonstersLeaders {
    BRINGER_OF_DEATH("Bringer of Death", "Double Close Combat Units", "Doubles the power of your close combat units unless there is a Commander's Horn in that row."),
    KING_OF_THE_WILD_HUNT("King of the Wild Hunt", "Take a Dead Card", "Select and take a card from your graveyard (cannot take a Hero card)."),
    DESTROYER_OF_WORLDS("Destroyer of Worlds", "Kill Two Cards to Draw One", "Destroy two of your own units and draw a new card from your deck."),
    COMMANDER_OF_THE_RED_RIDERS("Commander of the Red Riders", "Play a Weather Card", "Select and play a weather card from your deck."),
    THE_TREACHEROUS("The Treacherous", "Double Spy Cards", "Doubles the power of spy cards for both players.");

    private final String name;
    private final String ability;
    private final String description;

    MonstersLeaders(String name, String ability, String description) {
        this.name = name;
        this.ability = ability;
        this.description = description;
    }

    public Leader getLeader() {
        return new Leader(name, Faction.MONSTER, description);
    }
}
