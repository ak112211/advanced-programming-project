package enums.cards;

import model.abilities.Ability;
import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import model.abilities.Berserker;
import model.abilities.instantaneousabilities.Medic;
import model.abilities.instantaneousabilities.Muster;
import model.abilities.instantaneousabilities.Scorch;
import model.abilities.persistentabilities.CommandersHorn;
import model.abilities.persistentabilities.Mardroeme;
import model.abilities.persistentabilities.MoraleBoost;
import model.abilities.persistentabilities.TightBond;
import model.card.Card;

public enum SkelligeCards {
    Mardroeme("Mardroeme", 0, 3, Type.SPELL, new Mardroeme(), Description.Mardroeme, false),
    BERSERKER("Berserker", 4, 1, Type.CLOSE_COMBAT_UNIT, new Berserker(), Description.TRANSFORMS_TO_VIDKAARL, false),
    VIDKAARL("Vidkaarl", 14, 0, Type.CLOSE_COMBAT_UNIT, new MoraleBoost(), Description.PLAYED_WITH_BERSERKER, false),
    SVANRIGE("Svanrige", 4, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    UDALRYK("Udalryk", 4, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    DONAR_AN_HINDAR("Donar an Hindar", 4, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    CLAN_AN_CRAITE("Clan An Craite", 6, 3, Type.CLOSE_COMBAT_UNIT, new TightBond(), Description.NOTHING, false),
    BLUEBOY_LUGOS("Blueboy Lugos", 6, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    MADMAN_LUGOS("Madman Lugos", 6, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    CERYS("Cerys", 10, 1, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.SUMMONS_SHIELD_MAIDEN, true),
    KAMBI("Kambi", 11, 1, Type.CLOSE_COMBAT_UNIT, new Transformers(), Description.TURNS_INTO_STRONGER_CARD, true),
    BIRNA_BRAN("Birna Bran", 2, 1, Type.CLOSE_COMBAT_UNIT, new Medic(), Description.NOTHING, false),
    CLAN_DRUMMOND_SHIELDMAIDEN("Clan Drummond Shieldmaiden", 4, 3, Type.CLOSE_COMBAT_UNIT, new TightBond(), Description.MUSTERED_BY_CERYS, false),
    CLAN_TORDARROCH_ARMORSMITH("Clan Tordarroch Armorsmith", 4, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    CLAN_DIMUN_PIRATE("Clan Dimun Pirate", 6, 1, Type.RANGED_UNIT, new Scorch(), Description.KILLS_STRONGEST_OPPONENT, false),
    CLAN_BROKVAR_ARCHER("Clan Brokvar Archer", 6, 3, Type.RANGED_UNIT, null, Description.NOTHING, false),
    ERMION("Ermion", 8, 1, Type.RANGED_UNIT, new Mardroeme(), Description.NOTHING, true),
    HJALMAR("Hjalmar", 10, 1, Type.RANGED_UNIT, null, Description.NOTHING, true),
    YOUNG_BERSERKER("Young Berserker", 2, 3, Type.RANGED_UNIT, new Berserker(), Description.TRANSFORMS_TO_YOUNG_VIDKAARL, false),
    YOUNG_VIDKAARL("Young Vidkaarl", 8, 0, Type.RANGED_UNIT, new TightBond(), Description.PLAYED_WITH_YOUNG_BERSERKER, false),
    LIGHT_LONGSHIP("Light Longship", 4, 3, Type.RANGED_UNIT, new Muster(), Description.NOTHING, false),
    HOLGER_BLACKHAND("Holger Blackhand", 4, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false),
    WAR_LONGSHIP("War Longship", 6, 3, Type.SIEGE_UNIT, new TightBond(), Description.NOTHING, false),
    DRAIG_BON_DHU("Draig Bon-Dhu", 2, 1, Type.SIEGE_UNIT, new CommandersHorn(), Description.NOTHING, false),
    OLAF("Olaf", 12, 1, Type.AGILE_UNIT, new MoraleBoost(), Description.NOTHING, false);

    private final String NAME;
    private final int POWER;
    private final int NO_OF_CARDS_IN_GAME;
    private final Type TYPE;
    private final Ability ABILITY;
    private final Description DESCRIPTION;
    private final boolean IS_HERO;

    SkelligeCards(String name, int power, int noOfCardsInGame, Type type, Ability ability, Description description, boolean isHero) {
        NAME = name;
        POWER = power;
        NO_OF_CARDS_IN_GAME = noOfCardsInGame;
        TYPE = type;
        ABILITY = ability;
        DESCRIPTION = description;
        IS_HERO = isHero;
    }

    public Card getCard() {
        return new Card(NAME, TYPE, NO_OF_CARDS_IN_GAME, POWER, ABILITY, IS_HERO, Faction.SKELLIGE, DESCRIPTION);
    }
}
