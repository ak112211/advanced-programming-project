package enums.leaders;

import enums.cardsinformation.Faction;
import model.card.Leader;

public enum RealmsNorthernLeaders {
    THE_SIEGEMASTER("The Siegemaster", "Play Impenetrable Fog", "Select and play an Impenetrable Fog card from your deck. If no such card is in your deck, the turn passes to the opponent without any effect."),
    THE_STEEL_FORGED("The Steel-Forged", "Clear Weather Effects", "Removes the effects of Impenetrable Fog, Biting Frost, and Torrential Rain."),
    KING_OF_TEMERIA("King of Temeria", "Double Siege Units", "Doubles the power of your siege units unless there is a Commander's Horn in that row."),
    LORD_COMMANDER_OF_THE_NORTH("Lord Commander of the North", "Destroy Strongest Enemy Siege", "Destroys the strongest siege unit if the opponent's siege units have a total power greater than 10."),
    SON_OF_MEDELL("Son of Medell", "Destroy Strongest Enemy Ranged", "Destroys the strongest ranged unit if the opponent's ranged units have a total power greater than 10.");

    private final String name;
    private final String description;

    RealmsNorthernLeaders(String name, String ability, String description) {
        this.name = name;
        this.description = description;
    }

    public Leader getLeader() {
        return new Leader(name, Faction.REALMS_NORTHERN, description);
    }
}
