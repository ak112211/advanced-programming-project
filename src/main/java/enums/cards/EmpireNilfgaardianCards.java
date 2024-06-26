package enums.cards;

import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.Medic;
import model.abilities.instantaneousabilities.Spy;
import model.abilities.persistentabilities.TightBond;
import model.card.Card;

public enum EmpireNilfgaardianCards implements CardEnum {
    IMPERA_BRIGADE_GUARD("Imperal Brigade Guard", 3, 4, Type.CLOSE_COMBAT_UNIT, new TightBond(), Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_imperal_brigade.jpg"),
    STEFAN_SKELLEN("Stefan Skellen", 9, 1, Type.CLOSE_COMBAT_UNIT, new Spy(), Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_stefan.jpg"),
    YOUNG_EMISSARY("Young Emissary", 5, 2, Type.CLOSE_COMBAT_UNIT, new TightBond(), Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_young_emissary.jpg"),
    CAHIR_MAWR_DYFFRYN_AEP_CEALLACH("Cahir Mawr Dyffryn aep Ceallach", 6, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_cahir.jpg"),
    VATTIER_DE_RIDEAUX("Vattier de Rideaux", 4, 1, Type.CLOSE_COMBAT_UNIT, new Spy(), Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_vattier.jpg"),
    MENNO_COEHOORN("Menno Coehoorn", 10, 1, Type.CLOSE_COMBAT_UNIT, new Medic(), Description.NOTHING, true, "/gwentImages/img/lg/nilfgaard_menno.jpg"),
    PUTTKAMMER("Puttkammer", 3, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_puttkammer.jpg"),
    BLACK_INFANTRY_ARCHER("Black Infantry Archer", 10, 2, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_black_archer.jpg"),
    TIBOR_EGGEGBRACHT("Tibor Eggebracht", 10, 1, Type.RANGED_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/nilfgaard_tibor.jpg"),
    RENUALD_AEP_MATSEN("Renuald aep Matsen", 5, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_renuald.jpg"),
    FRINGILLA_VIGO("Fringilla Vigo", 6, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_fringilla.jpg"),
    ROTTEN_MANGONEL("Rotten Mangonel", 3, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_rotten.jpg"),
    ZERRIKANIAN_FIRE_SCORPION("Zerrikanian Fire Scorpion", 5, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_zerri.jpg"),
    SIEGE_ENGINEER("Siege Engineer", 6, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_siege_engineer.jpg"),
    MORVRAN_VOORHIS("Morvran Voorhis", 10, 1, Type.SIEGE_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/nilfgaard_moorvran.jpg"),
    CYNTHIA("Cynthia", 4, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_cynthia.jpg"),
    ETOLIAN_AUXILIARY_ARCHERS("Etolian Auxiliary Archers", 1, 2, Type.RANGED_UNIT, new Medic(), Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_archer_support.jpg"),
    MORTEISEN("Morteisen", 3, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_morteisen.jpg"),
    NAUSICA_CAVALRY_RIDER("Nausicaa Cavalry Rider", 2, 3, Type.CLOSE_COMBAT_UNIT, new TightBond(), Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_nauzicaa_2.jpg"),
    SIEGE_TECHNICIAN("Siege Technician", 0, 1, Type.SIEGE_UNIT, new Medic(), Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_siege_support.jpg"),
    SWEERS("Sweers", 2, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_sweers.jpg"),
    VANHEMAR("Vanhemar", 4, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_vanhemar.jpg"),
    VREEMDE("Vreemde", 2, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_vreemde.jpg"),
    SHILARD_FITZ_OESTERLEN("Shilard Fitz-Oesterlen", 7, 1, Type.CLOSE_COMBAT_UNIT, new Spy(), Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_shilard.jpg"),
    ASSIRE_VAR_ANAHID("Assire var Anahid", 6, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_assire.jpg"),
    HEAVY_ZERRIKANIAN_FIRE_SCORPION("Heavy Zerrikanian Fire Scorpion", 10, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_heavy_zerri.jpg"),
    ALBRICH("Albrich", 2, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_albrich.jpg"),
    LETHO_OF_GULET("Letho of Gulet", 10, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/nilfgaard_letho.jpg"),
    RAINFARN("Rainfarn", 4, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/nilfgaard_rainfarn.jpg");
    private final String NAME;
    private final int POWER;
    private final int NO_OF_CARDS_IN_GAME;
    private final Type TYPE;
    private final Ability ABILITY;
    private final Description DESCRIPTION;
    private final boolean IS_HERO;
    private final String IMAGE_PATH;

    EmpireNilfgaardianCards(String name, int power, int noOfCardsInGame, Type type, Ability ability, Description description, boolean isHero, String imagePath) {
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
