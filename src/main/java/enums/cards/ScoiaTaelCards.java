package enums.cards;

import model.abilities.Ability;
import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import model.abilities.instantaneousabilities.Medic;
import model.abilities.instantaneousabilities.Muster;
import model.abilities.instantaneousabilities.Scorch;
import model.abilities.persistentabilities.MoraleBoost;
import model.card.Card;

public enum ScoiaTaelCards implements CardEnum {
    ELVEN_SKIRMISHER("Elven Skirmisher", 2, 3, Type.RANGED_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_elf_skirmisher.jpg"),
    IORVETH("Iorveth", 10, 1, Type.RANGED_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/scoiatael_iorveth.jpg"),
    YAEVINN("Yaevinn", 6, 1, Type.AGILE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_yaevinn.jpg"),
    CIARAN_AEP("Ciaran aep", 3, 1, Type.AGILE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_ciaran.jpg"),
    DENNIS_CRANMER("Dennis Cranmer", 6, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_dennis.jpg"),
    DOL_BLATHANNA_SCOUT("Dol Blathanna Scout", 6, 3, Type.AGILE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_dol_infantry.jpg"),
    DOL_BLATHANNA_ARCHER("Dol Blathanna Archer", 4, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_dol_archer.jpg"),
    DWARVEN_SKIRMISHER("Dwarven Skirmisher", 3, 3, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_dwarf.jpg"),
    FILAVANDREL("Filavandrel", 6, 1, Type.AGILE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_filavandrel.jpg"),
    HAVEKAR_HEALER("Havekar Healer", 0, 3, Type.RANGED_UNIT, new Medic(), Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_havekar_nurse.jpg"),
    HAVEKAR_SMUGGLER("Havekar Smuggler", 5, 3, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_havekar_support.jpg"),
    IDA_EMEAN_AEP("Ida Emean aep", 6, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_ida.jpg"),
    RIORDAIN("Riordain", 1, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_riordain.jpg"),
    TORUVIEL("Toruviel", 2, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_toruviel.jpg"),
    VRIHEDD_BRIGADE_RECRUIT("Vrihedd Brigade Recruit", 4, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_vrihedd_cadet.jpg"),
    MAHAKAMAN_DEFENDER("Mahakaman Defender", 5, 5, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_mahakam.jpg"),
    VRIHEDD_BRIGADE_VETERAN("Vrihedd Brigade Veteran", 5, 2, Type.AGILE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_vrihedd_brigade.jpg"),
    MILVA("Milva", 10, 1, Type.RANGED_UNIT, new MoraleBoost(), Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_milva.jpg"),
    SEASENTHESSIS("Seasenthessis", 10, 1, Type.RANGED_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/scoiatael_saskia.jpg"),
    SCHIRRU("Schirru", 8, 1, Type.SIEGE_UNIT, new Scorch(Type.SIEGE_UNIT), Description.KILLS_STRONGEST_ENEMY, false, "/gwentImages/img/lg/scoiatael_schirru.jpg"),
    BARCLAY_ELS("Barclay Els", 6, 1, Type.AGILE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/scoiatael_barclay.jpg"),
    EITHNE("Eithne", 10, 1, Type.RANGED_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/scoiatael_eithne.jpg"),
    ISENGRIM_FAOILTIARNA("Isengrim Faoiltiarna", 10, 1, Type.CLOSE_COMBAT_UNIT, new MoraleBoost(), Description.NOTHING, true, "/gwentImages/img/lg/scoiatael_isengrim.jpg");

    private final String IMAGE_PATH;
    private final String NAME;
    private final int POWER;
    private final int NO_OF_CARDS_IN_GAME;
    private final Type TYPE;
    private final Ability ABILITY;
    private final Description DESCRIPTION;
    private final boolean IS_HERO;

    ScoiaTaelCards(String name, int power, int noOfCardsInGame, Type type, Ability ability, Description description, boolean isHero, String imagePath) {
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
        return new Card(NAME, TYPE, NO_OF_CARDS_IN_GAME, POWER, ABILITY, IS_HERO, Faction.SKELLIGE, DESCRIPTION, IMAGE_PATH);
    }
}
