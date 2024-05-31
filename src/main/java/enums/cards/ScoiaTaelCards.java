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

public enum ScoiaTaelCards {
    ELVEN_SKIRMISHER("Elven Skirmisher", 2, 3, Type.RANGED_UNIT, new Muster(), Description.NOTHING, false),
    IORVETH("Iorveth", 10, 1, Type.RANGED_UNIT, null, Description.NOTHING, true),
    YAEVINN("Yaevinn", 6, 1, Type.AGILE_UNIT, null, Description.NOTHING, false),
    CIARAN_AEP("Ciaran aep", 3, 1, Type.AGILE_UNIT, null, Description.NOTHING, false),
    DENNIS_CRANMER("Dennis Cranmer", 6, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    DOL_BLATHANNA_SCOUT("Dol Blathanna Scout", 6, 3, Type.AGILE_UNIT, null, Description.NOTHING, false),
    DOL_BLATHANNA_ARCHER("Dol Blathanna Archer", 4, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    DWARVEN_SKIRMISHER("Dwarven Skirmisher", 3, 3, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false),
    FILAVANDREL("Filavandrel", 6, 1, Type.AGILE_UNIT, null, Description.NOTHING, false),
    HAVEKAR_HEALER("Havekar Healer", 0, 3, Type.RANGED_UNIT, new Medic(), Description.NOTHING, false),
    HAVEKAR_SMUGGLER("Havekar Smuggler", 5, 3, Type.CLOSE_COMBAT_UNIT, new Muster(), Description.NOTHING, false),
    IDA_EMEAN_AEP("Ida Emean aep", 6, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    RIORDAIN("Riordain", 1, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    TORUVIEL("Toruviel", 2, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    VRIHEDD_BRIGADE_RECRUIT("Vrihedd Brigade Recruit", 4, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    MAHAKAMAN_DEFENDER("Mahakaman Defender", 5, 5, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    VRIHEDD_BRIGADE_VETERAN("Vrihedd Brigade Veteran", 5, 2, Type.AGILE_UNIT, null, Description.NOTHING, false),
    MILVA("Milva", 10, 1, Type.RANGED_UNIT, new MoraleBoost(), Description.NOTHING, false),
    SEASENTHESSIS("Seasenthessis", 10, 1, Type.RANGED_UNIT, null, Description.NOTHING, true),
    SCHIRRU("Schirru", 8, 1, Type.SIEGE_UNIT, new Scorch(), Description.KILLS_STRONGEST_ENEMY, false),
    BARCLAY_ELS("Barclay Els", 6, 1, Type.AGILE_UNIT, null, Description.NOTHING, false),
    EITHNE("Eithne", 10, 1, Type.RANGED_UNIT, null, Description.NOTHING, true),
    ISENGRIM_FAOILTIARNA("Isengrim Faoiltiarna", 10, 1, Type.CLOSE_COMBAT_UNIT, new MoraleBoost(), Description.NOTHING, true);

    private final String name;
    private final int power;
    private final int noOfCardsInGame;
    private final Type type;
    private final Ability ability;
    private final Description description;
    private final boolean isHero;

    ScoiaTaelCards(String name, int power, int noOfCardsInGame, Type type, Ability ability, Description description, boolean isHero) {
        this.name = name;
        this.power = power;
        this.noOfCardsInGame = noOfCardsInGame;
        this.type = type;
        this.ability = ability;
        this.description = description;
        this.isHero = isHero;
    }

    public Card getCard() {
        return new Card(name, type, noOfCardsInGame, power, ability, isHero, Faction.SCOIA_TAEL, description);
    }
}
