package enums.cards;

import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.Muster;
import model.abilities.instantaneousabilities.Scorch;
import model.abilities.persistentabilities.MoraleBoost;
import model.card.Card;

public enum MonstersCards {
    DRAUG("Draug", 10, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, true),
    LESHEN("Leshen", 10, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, true),
    KAYRAN("Kayran", 8, 1, Type.AGILE_UNIT, new MoraleBoost(), Description.NOTHING, true),
    TOAD("Toad", 7, 1, Type.RANGED_UNIT, new Scorch(), Description.KILLS_UNITS, false),
    ARACHAS_BEHEMOTH("Arachas Behemoth", 6, 1, Type.SIEGE_UNIT, new Muster(), Description.NOTHING, false),
    CRONE_WEAVESS("Crone: Weavess", 6, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false),
    CRONE_WHISPESS("Crone: Whispess", 6, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false),
    EARTH_ELEMENTAL("Earth Elemental", 6, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false),
    FIEND("Fiend", 6, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    FIRE_ELEMENTAL("Fire Elemental", 6, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false),
    FORKTAIL("Forktail", 5, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    GRAVE_HAG("Grave Hag", 5, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    GRIFFIN("Griffin", 5, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    ICE_GIANT("Ice Giant", 5, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false),
    PLAGUE_MAIDEN("Plague Maiden", 5, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    VAMPIRE_KATAKAN("Vampire: Katakan", 5, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false),
    WEREWOLF("Werewolf", 5, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    ARACHAS("Arachas", 4, 3, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false),
    VAMPIRE_BRUXA("Vampire: Bruxa", 4, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false),
    VAMPIRE_EKIMMARA("Vampire: Ekimmara", 4, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false),
    VAMPIRE_FLEDER("Vampire: Fleder", 4, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false),
    VAMPIRE_GARKAIN("Vampire: Garkain", 4, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false),
    COCKATRICE("Cockatrice", 2, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    ENDREGA("Endrega", 2, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    FOGLET("Foglet", 2, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    HARPY("Harpy", 2, 1, Type.AGILE_UNIT, null, Description.NOTHING, false),
    NEKKER("Nekker", 2, 3, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false),
    WYVERN("Wyvern", 2, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    GHOUL("Harpy", 1, 3, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false);

    private String name;
    private int power;
    private int noOfCardsInGame;
    private Type type;
    private Ability ability;
    private Description description;
    private boolean isHero;

    MonstersCards(String name, int power, int noOfCardsInGame, Type type, Ability ability, Description description, boolean isHero) {
        this.name = name;
        this.power = power;
        this.noOfCardsInGame = noOfCardsInGame;
        this.type = type;
        this.ability = ability;
        this.description = description;
        this.isHero = isHero;
    }

    public Card getCard() {
        return new Card(name, type, noOfCardsInGame, power, ability, isHero, Faction.MONSTER, description);
    }
}