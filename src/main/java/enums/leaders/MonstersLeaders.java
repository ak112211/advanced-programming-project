package enums.leaders;

import enums.cardsinformation.Faction;
import model.abilities.Ability;
import model.card.Leader;

public enum MonstersLeaders {
    BRINGER_OF_DEATH("Bringer of Death", "Double Close Combat Units", "Doubles the power of your close combat units unless there is a Commander's Horn in that row.", "/gwentImages/img/lg/monsters_eredin_silver.jpg"),
    KING_OF_THE_WILD_HUNT("King of the Wild Hunt", "Take a Dead Card", "Select and take a card from your graveyard (cannot take a Hero card).", "/gwentImages/img/lg/monsters_eredin_bronze.jpg"),
    DESTROYER_OF_WORLDS("Destroyer of Worlds", "Kill Two Cards to Draw One", "Destroy two of your own units and draw a new card from your deck.", "/gwentImages/img/lg/monsters_eredin_gold.jpg"),
    COMMANDER_OF_THE_RED_RIDERS("Commander of the Red Riders", "Play a Weather Card", "Select and play a weather card from your deck.", "/gwentImages/img/lg/monsters_eredin_copper.jpg"),
    THE_TREACHEROUS("The Treacherous", "Double Spy Cards", "Doubles the power of spy cards for both players.", "/gwentImages/img/lg/monsters_eredin_the_treacherous.jpg");

    private final String name;
    private final Ability ability;
    private final String description;
    private final String imagePath;

    MonstersLeaders(String name, Ability ability, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.ability = ability;
        this.imagePath = imagePath;
    }

    public Leader getLeader() {
        return new Leader(name, ability,Faction.MONSTER, description, imagePath);
    }
}
