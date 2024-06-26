package enums.leaders;

import enums.cards.NeutralCards;
import enums.cardsinformation.CardsPlace;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.PlayCard;
import model.abilities.instantaneousabilities.Scorch;
import model.card.Leader;

public enum RealmsNorthernLeaders {
    THE_SIEGEMASTER("The Siegemaster", new PlayCard(null, NeutralCards.IMPENETRABLE_FOG), "Select and play an Impenetrable Fog card from your deck. If no such card is in your deck, the turn passes to the opponent without any effect.", "/gwentImages/img/lg/realms_foltest_silver.jpg"),
    THE_STEEL_FORGED("The Steel-Forged", new PlayCard(null, NeutralCards.CLEAR_WEATHER), "Removes the effects of Impenetrable Fog, Biting Frost, and Torrential Rain.", "/gwentImages/img/lg/realms_foltest_gold.jpg"),
    KING_OF_TEMERIA("King of Temeria", new PlayCard(null, NeutralCards.COMMANDERS_HORN, Type.SIEGE_UNIT), "Doubles the power of your siege units unless there is a Commander's Horn in that row.", "/gwentImages/img/lg/realms_foltest_copper.jpg"),
    LORD_COMMANDER_OF_THE_NORTH("Lord Commander of the North", new Scorch(Type.SIEGE_UNIT), "Destroys the strongest siege unit if the opponent's siege units have a total power greater than 10.", "/gwentImages/img/lg/realms_foltest_bronze.jpg"),
    SON_OF_MEDELL("Son of Medell", new Scorch(Type.RANGED_UNIT), "Destroys the strongest ranged unit if the opponent's ranged units have a total power greater than 10.", "/gwentImages/img/lg/realms_foltest_son_of_medell.jpg");

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
