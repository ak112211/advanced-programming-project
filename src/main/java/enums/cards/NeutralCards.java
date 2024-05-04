package enums.cards;

import enums.cardsinformation.Ability;
import enums.cardsinformation.Description;
import enums.cardsinformation.Type;
import model.card.Card;

public enum NeutralCards {
    BITING_FROST("Biting Frost", 0, 3, Type.WEATHER, Ability.NOTHING, Description.SETS_CLOSE_COMBAT_TO_ONE, false),
    IMPENETRABLE_FOG("Impenetrable Fog", 0, 3, Type.WEATHER, Ability.NOTHING, Description.SETS_RANGED_TO_ONE, false),
    COMMANDERS_HORN("Commander’s Horn", 0, 3, Type.SPELL, Ability.NOTHING, Description.DOUBLES_POWER_IN_ROW, false),
    DECOY("Decoy", 0, 3, Type.SPELL, Ability.NOTHING, Description.DECOY_CARD, false),
    COW("Cow", 0, 1, Type.RANGED_UNIT, Ability.TRANSFORMERS, Description.BECOMES_STRONGER_NEXT_ROUND, false),
    EMIEL_REGIS("Emiel Regis", 5, 1, Type.CLOSE_COMBAT_UNIT, Ability.NOTHING, Description.NOTHING, false),
    GAUNTER_ODIMM("Gaunter O’Dimm", 2, 1, Type.SIEGE_UNIT, Ability.MUSTER, Description.NOTHING, false),
    GERALT_OF_RIVIA("Geralt of Rivia", 15, 1, Type.CLOSE_COMBAT_UNIT, Ability.NOTHING, Description.NOTHING, true),
    MYSTERIOUS_ELF("Mysterious Elf", 0, 1, Type.CLOSE_COMBAT_UNIT, Ability.SPY, Description.NOTHING, true),
    OLGIERD_VON_EVEREC("Olgierd Von Everec", 6, 1, Type.AGILE_UNIT, Ability.MORAL_BOOSTS, Description.NOTHING, false),
    VESEMIR("Vesemir", 6, 1, Type.CLOSE_COMBAT_UNIT, Ability.NOTHING, Description.NOTHING, false),
    VILLENTRETENMERTH("Villentretenmerth", 7, 1, Type.CLOSE_COMBAT_UNIT, Ability.SCORCH, Description.SCOTCH_ON_ENEMY_CLOSE_COMBAT, false),
    YENNEFER_OF_VENGERBERG("Yennefer of Vengerberg", 7, 1, Type.RANGED_UNIT, Ability.MEDIC, Description.NOTHING, true),
    ZOLTAN_CHIVAY("Zoltan Chivay", 5, 1, Type.CLOSE_COMBAT_UNIT, Ability.NOTHING, Description.NOTHING, false);

    private final String name;
    private final int power;
    private final int noOfCardsInGame;
    private final Type type;
    private final Ability ability;
    private final Description description;
    private final boolean isHero;

    NeutralCards(String name, int power, int noOfCardsInGame, Type type, Ability ability, Description description, boolean isHero) {
        this.name = name;
        this.power = power;
        this.noOfCardsInGame = noOfCardsInGame;
        this.type = type;
        this.ability = ability;
        this.description = description;
        this.isHero = isHero;
    }

    public Card getCard() {
        return new Card(name, type, noOfCardsInGame, power, ability, isHero, null, description);
    }
}
