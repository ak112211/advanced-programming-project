package enums.cards;

import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.Medic;
import model.abilities.instantaneousabilities.Spy;
import model.abilities.persistentabilities.TightBond;
import model.card.Card;

public enum EmpireNilfgaardianCards {
    IMPERA_BRIGADE_GUARD("Impera Brigade Guard", 3, 4, Type.CLOSE_COMBAT_UNIT, new TightBond(), Description.NOTHING, false),
    STEFAN_SKELLEN("Stefan Skellen", 9, 1, Type.CLOSE_COMBAT_UNIT, new Spy(), Description.NOTHING, false),
    YOUNG_EMISSARY("Young Emissary", 5, 2, Type.CLOSE_COMBAT_UNIT, new TightBond(), Description.NOTHING, false),
    CAHIR_MAWR_DYFFRYN_AEP_CEALLACH("Cahir Mawr Dyffryn aep Ceallach", 6, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    VATTIER_DE_RIDEAUX("Vattier de Rideaux", 4, 1, Type.CLOSE_COMBAT_UNIT, new Spy(), Description.NOTHING, false),
    MENNO_COEHOORN("Menno Coehoorn", 10, 1, Type.CLOSE_COMBAT_UNIT, new Medic(), Description.NOTHING, true),
    PUTTKAMMER("Puttkammer", 3, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    BLACK_INFANTRY_ARCHER("Black Infantry Archer", 10, 2, Type.RANGED_UNIT, null, Description.NOTHING, false),
    TIBOR_EGGEGBRACHT("Tibor Eggebracht", 10, 1, Type.RANGED_UNIT, null, Description.NOTHING, true),
    RENUALD_AEP_MATSEN("Renuald aep Matsen", 5, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    FRINGILLA_VIGO("Fringilla Vigo", 6, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    ROTTEN_MANGONEL("Rotten Mangonel", 3, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false),
    ZERRIKANIAN_FIRE_SCORPION("Zerrikanian Fire Scorpion", 5, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false),
    SIEGE_ENGINEER("Siege Engineer", 6, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false),
    MORVRAN_VOORHIS("Morvran Voorhis", 10, 1, Type.SIEGE_UNIT, null, Description.NOTHING, true),
    CYNTHIA("Cynthia", 4, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    ETOLIAN_AUXILIARY_ARCHERS("Etolian Auxiliary Archers", 1, 2, Type.RANGED_UNIT, new Medic(), Description.NOTHING, false),
    MORTEISEN("Morteisen", 3, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false),
    NAUSICA_CAVALRY_RIDER("Nausicaa Cavalry Rider", 2, 3, Type.CLOSE_COMBAT_UNIT, new TightBond(), Description.NOTHING, false),
    SIEGE_TECHNICIAN("Siege Technician", 0, 1, Type.SIEGE_UNIT, new Medic(), Description.NOTHING, false),
    SWEERS("Sweers", 2, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    VANHEMAR("Vanhemar", 4, 1, Type.RANGED_UNIT, null, Description.NOTHING, false),
    VREEMDE("Vreemde", 2, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false);

    private final String name;
    private final int power;
    private final int noOfCardsInGame;
    private final Type type;
    private final Ability ability;
    private final Description description;
    private final boolean isHero;

    EmpireNilfgaardianCards(String name, int power, int noOfCardsInGame, Type type, Ability ability, Description description, boolean isHero) {
        this.name = name;
        this.power = power;
        this.noOfCardsInGame = noOfCardsInGame;
        this.type = type;
        this.ability = ability;
        this.description = description;
        this.isHero = isHero;
    }

    public Card getCard() {
        return new Card(name, type, noOfCardsInGame, power, ability, isHero, Faction.EMPIRE_NILFGAARDIAM, description);
    }
}
