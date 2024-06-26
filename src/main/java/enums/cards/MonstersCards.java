package enums.cards;

import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.Muster;
import model.abilities.instantaneousabilities.Scorch;
import model.abilities.persistentabilities.MoraleBoost;
import model.card.Card;

public enum MonstersCards implements CardEnum {
    DRAUG("Draug", 10, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/monsters_draug.jpg"),
    LESHEN("Leshen", 10, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/monsters_leshen.jpg"),
    KAYRAN("Kayran", 8, 1, Type.AGILE_UNIT, new MoraleBoost(), Description.NOTHING, true, "/gwentImages/img/lg/monsters_kayran.jpg"),
    TOAD("Toad", 7, 1, Type.RANGED_UNIT, new Scorch(Type.RANGED_UNIT), Description.KILLS_UNITS, false, "/gwentImages/img/lg/monsters_toad.jpg"),
    ARACHAS_BEHEMOTH("Arachas Behemoth", 6, 1, Type.SIEGE_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/monsters_arachas.jpg"),
    CRONE_WEAVESS("Crone: Weavess", 6, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/monsters_witch_velen_1.jpg"),
    CRONE_WHISPESS("Crone: Whispess", 6, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/monsters_witch_velen_2.jpg"),
    EARTH_ELEMENTAL("Earth Elemental", 6, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_earth_elemental.jpg"),
    FIEND("Fiend", 6, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_fiend.jpg"),
    FIRE_ELEMENTAL("Fire Elemental", 6, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_fire_elemental.jpg"),
    FORKTAIL("Forktail", 5, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_forktail.jpg"),
    GRAVE_HAG("Grave Hag", 5, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_gravehag.jpg"),
    GRIFFIN("Griffin", 5, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_gryffin.jpg"),
    ICE_GIANT("Ice Giant", 5, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_frost_giant.jpg"),
    PLAGUE_MAIDEN("Plague Maiden", 5, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_mighty_maiden.jpg"),
    VAMPIRE_KATAKAN("Vampire: Katakan", 5, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/monsters_katakan.jpg"),
    WEREWOLF("Werewolf", 5, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_werewolf.jpg"),
    ARACHAS("Arachas", 4, 3, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/monsters_arachas.jpg"),
    VAMPIRE_BRUXA("Vampire: Bruxa", 4, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/monsters_bruxa.jpg"),
    VAMPIRE_EKIMMARA("Vampire: Ekimmara", 4, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/monsters_ekkima.jpg"),
    VAMPIRE_FLEDER("Vampire: Fleder", 4, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/monsters_fleder.jpg"),
    VAMPIRE_GARKAIN("Vampire: Garkain", 4, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/monsters_garkain.jpg"),
    COCKATRICE("Cockatrice", 2, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_cockatrice.jpg"),
    ENDREGA("Endrega", 2, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_endrega.jpg"),
    FOGLET("Foglet", 2, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_fogling.jpg"),
    HARPY("Harpy", 2, 1, Type.AGILE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_harpy.jpg"),
    NEKKER("Nekker", 2, 3, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/monsters_nekker.jpg"),
    WYVERN("Wyvern", 2, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/monsters_wyvern.jpg"),
    GHOUL("Harpy", 1, 3, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/monsters_ghoul.jpg");

    private final String NAME;
    private final int POWER;
    private final int NO_OF_CARDS_IN_GAME;
    private final Type TYPE;
    private final Ability ABILITY;
    private final Description DESCRIPTION;
    private final boolean IS_HERO;
    private final String IMAGE_PATH;

    MonstersCards(String name, int power, int noOfCardsInGame, Type type, Ability ability, Description description, boolean isHero, String imagePath) {
        NAME = name;
        POWER = power;
        NO_OF_CARDS_IN_GAME = noOfCardsInGame;
        TYPE = type;
        ABILITY = ability;
        DESCRIPTION = description;
        IS_HERO = isHero;
        IMAGE_PATH = imagePath;
    }

    public Card getCard() {
        return new Card(NAME, TYPE, NO_OF_CARDS_IN_GAME, POWER, ABILITY, IS_HERO, Faction.SKELLIGE, DESCRIPTION, IMAGE_PATH, this);
    }
}