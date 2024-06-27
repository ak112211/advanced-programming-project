package enums.cards;

import model.abilities.Ability;
import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import model.abilities.Berserker;
import model.abilities.ejectabilities.SummonAvenger;
import model.abilities.instantaneousabilities.Medic;
import model.abilities.instantaneousabilities.Muster;
import model.abilities.instantaneousabilities.Scorch;
import model.abilities.persistentabilities.CommandersHorn;
import model.abilities.persistentabilities.Mardroeme;
import model.abilities.persistentabilities.MoraleBoost;
import model.abilities.persistentabilities.TightBond;
import model.card.Card;

public enum SkelligeCards implements CardEnum {
    MARDROEME("Mardroeme", 0, 3, Type.SPELL, new Mardroeme(), Description.Mardroeme, false, "/gwentImages/img/lg/special_mardroeme.jpg"),
    VILDKAARL("Vildkaarl", 14, 0, Type.CLOSE_COMBAT_UNIT, new MoraleBoost(), Description.PLAYED_WITH_BERSERKER, false, "/gwentImages/img/lg/skellige_vildkaarl.jpg"),
    BERSERKER("Berserker", 4, 1, Type.CLOSE_COMBAT_UNIT, new Berserker(SkelligeCards.VILDKAARL), Description.TRANSFORMS_TO_VILDKAARL, false, "/gwentImages/img/lg/skellige_berserker.jpg"),
    SVANRIGE("Svanrige", 4, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/skellige_svanrige.jpg"),
    UDALRYK("Udalryk", 4, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/skellige_udalryk.jpg"),
    DONAR_AN_HINDAR("Donar an Hindar", 4, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/skellige_donar.jpg"),
    CLAN_AN_CRAITE("Clan An Craite", 6, 3, Type.CLOSE_COMBAT_UNIT, new TightBond(), Description.NOTHING, false, "/gwentImages/img/lg/skellige_craite_warrior.jpg"),
    BLUEBOY_LUGOS("Blueboy Lugos", 6, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/skellige_blueboy.jpg"),
    MADMAN_LUGOS("Madman Lugos", 6, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/skellige_madmad_lugos.jpg"),
    CERYS("Cerys", 10, 1, Type.CLOSE_COMBAT_UNIT, new Muster("Clan Drummond Shieldmaiden"), Description.SUMMONS_SHIELD_MAIDEN, true, "/gwentImages/img/lg/skellige_cerys.jpg"),
    KAMBI("Kambi", 11, 1, Type.CLOSE_COMBAT_UNIT, new SummonAvenger(null), Description.TURNS_INTO_STRONGER_CARD, true, "/gwentImages/img/lg/skellige_kambi.jpg"),
    BIRNA_BRAN("Birna Bran", 2, 1, Type.CLOSE_COMBAT_UNIT, new Medic(), Description.NOTHING, false, "/gwentImages/img/lg/skellige_birna.jpg"),
    CLAN_DRUMMOND_SHIELDMAIDEN("Clan Drummond Shieldmaiden", 4, 3, Type.CLOSE_COMBAT_UNIT, new TightBond(), Description.MUSTERED_BY_CERYS, false, "/gwentImages/img/lg/skellige_shield_maiden.jpg"),
    CLAN_TORDARROCH_ARMORSMITH("Clan Tordarroch Armorsmith", 4, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/skellige_tordarroch.jpg"),
    CLAN_DIMUN_PIRATE("Clan Dimun Pirate", 6, 1, Type.RANGED_UNIT, new Scorch(Type.SPY_UNIT), Description.KILLS_STRONGEST_OPPONENT, false, "/gwentImages/img/lg/skellige_dimun_pirate.jpg"),
    CLAN_BROKVAR_ARCHER("Clan Brokvar Archer", 6, 3, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/skellige_brokva_archer.jpg"),
    ERMION("Ermion", 8, 1, Type.RANGED_UNIT, new Mardroeme(), Description.NOTHING, true, "/gwentImages/img/lg/skellige_ermion.jpg"),
    HJALMAR("Hjalmar", 10, 1, Type.RANGED_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/skellige_hjalmar.jpg"),
    YOUNG_VILDKAARL("Young Vildkaarl", 8, 0, Type.RANGED_UNIT, new TightBond(), Description.PLAYED_WITH_YOUNG_BERSERKER, false, "/gwentImages/img/lg/skellige_young_vildkaarl.jpg"),
    YOUNG_BERSERKER("Young Berserker", 2, 3, Type.RANGED_UNIT, new Berserker(SkelligeCards.YOUNG_VILDKAARL), Description.TRANSFORMS_TO_YOUNG_VILDKAARL, false, "/gwentImages/img/lg/skellige_young_berserker.jpg"),
    LIGHT_LONGSHIP("Light Longship", 4, 3, Type.RANGED_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/skellige_light_longship.jpg"),
    HOLGER_BLACKHAND("Holger Blackhand", 4, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/skellige_holger.jpg"),
    WAR_LONGSHIP("War Longship", 6, 3, Type.SIEGE_UNIT, new TightBond(), Description.NOTHING, false, "/gwentImages/img/lg/skellige_war_longship.jpg"),
    DRAIG_BON_DHU("Draig Bon-Dhu", 2, 1, Type.SIEGE_UNIT, new CommandersHorn(), Description.NOTHING, false, "/gwentImages/img/lg/skellige_draig.jpg"),
    OLAF("Olaf", 12, 1, Type.AGILE_UNIT, new MoraleBoost(), Description.NOTHING, false, "/gwentImages/img/lg/skellige_olaf.jpg");

    private final String NAME;
    private final int POWER;
    private final int NO_OF_CARDS_IN_GAME;
    private final Type TYPE;
    private final Ability ABILITY;
    private final Description DESCRIPTION;
    private final boolean IS_HERO;
    private final String IMAGE_PATH;

    SkelligeCards(String name, int power, int noOfCardsInGame, Type type, Ability ability, Description description, boolean isHero, String imagePath) {
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
