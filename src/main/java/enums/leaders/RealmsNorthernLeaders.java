package enums.leaders;

import enums.cardsinformation.Faction;
import model.abilities.Ability;
import model.card.Leader;

public enum RealmsNorthernLeaders {
    THE_SIEGEMASTER("The Siegemaster", "Play Impenetrable Fog", "Select and play an Impenetrable Fog card from your deck. If no such card is in your deck, the turn passes to the opponent without any effect.", "/gwentImages/img/lg/realms_foltest_silver.jpg"),
    THE_STEEL_FORGED("The Steel-Forged", "Clear Weather Effects", "Removes the effects of Impenetrable Fog, Biting Frost, and Torrential Rain.", "/gwentImages/img/lg/realms_foltest_gold.jpg"),
    KING_OF_TEMERIA("King of Temeria", "Double Siege Units", "Doubles the power of your siege units unless there is a Commander's Horn in that row.", "/gwentImages/img/lg/realms_foltest_copper.jpg"),
    LORD_COMMANDER_OF_THE_NORTH("Lord Commander of the North", "Destroy Strongest Enemy Siege", "Destroys the strongest siege unit if the opponent's siege units have a total power greater than 10.", "/gwentImages/img/lg/realms_foltest_bronze.jpg"),
    SON_OF_MEDELL("Son of Medell", "Destroy Strongest Enemy Ranged", "Destroys the strongest ranged unit if the opponent's ranged units have a total power greater than 10.", "/gwentImages/img/lg/realms_foltest_son_of_medell.jpg");

    private final String name;
    private final String description;
    private final String imagePath;
    private Ability ability;

    RealmsNorthernLeaders(String name, Ability ability, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.ability = ability;
        this.imagePath = imagePath;
    }

    public Leader getLeader() {
        return new Leader(name, ability, Faction.REALMS_NORTHERN, description, imagePath);
    }
}
