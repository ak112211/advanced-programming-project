package enums.cards;

import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.Medic;
import model.abilities.instantaneousabilities.Spy;
import model.abilities.persistentabilities.MoraleBoost;
import model.abilities.persistentabilities.TightBond;
import model.card.Card;

public enum RealmsNorthernCards implements CardEnum {
    BALLISTA("Ballista", 6, 2, Type.SIEGE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/realms_ballista.jpg"),
    BLUE_STRIPES_COMMANDO("Blue Stripes Commando", 4, 3, Type.CLOSE_COMBAT_UNIT, new TightBond(), Description.NOTHING, false, "/gwentImages/img/lg/realms_blue_stripes.jpg"),
    CATAPULT("Catapult", 8, 2, Type.SIEGE_UNIT, new TightBond(), Description.NOTHING, false, "/gwentImages/img/lg/realms_catapult_1.jpg"),
    DRAGON_HUNTER("Dragon Hunter", 5, 3, Type.RANGED_UNIT, new TightBond(), Description.NOTHING, false, "/gwentImages/img/lg/realms_dragon_hunter.jpg"),
    DETHMOLD("Dethmold", 6, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/realms_dethmold.jpg"),
    DUN_BANNER_MEDIC("Dun Banner Medic", 5, 1, Type.SIEGE_UNIT, new Medic(), Description.NOTHING, false, "/gwentImages/img/lg/realms_banner_nurse.jpg"),
    ESTERAD_THYSSEN("Esterad Thyssen", 10, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/realms_esterad.jpg"),
    JOHN_NATALIS("John Natalis", 10, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/realms_natalis.jpg"),
    KAEDWENI_SIEGE_EXPERT("Kaedweni Siege Expert", 1, 3, Type.SIEGE_UNIT, new MoraleBoost(), Description.NOTHING, false, "/gwentImages/img/lg/realms_kaedwen_siege.jpg"),
    KEIRA_METZ("Keira Metz", 5, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/realms_keira.jpg"),
    PHILIPPA_EILHART("Philippa Eilhart", 10, 1, Type.RANGED_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/realms_philippa.jpg"),
    POOR_FUCKING_INFANTRY("Poor Fucking Infantry", 1, 4, Type.CLOSE_COMBAT_UNIT, new TightBond(), Description.NOTHING, false, "/gwentImages/img/lg/realms_poor_infantry.jpg"),
    PRINCE_STENNIS("Prince Stennis", 5, 1, Type.CLOSE_COMBAT_UNIT, new Spy(), Description.NOTHING, false, "/gwentImages/img/lg/realms_stennis.jpg"),
    REDANIAN_FOOT_SOLDIER("Redanian Foot Soldier", 1, 2, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/realms_redania.jpg"),
    SABRINA_GLEVISSING("Sabrina Glevissing", 4, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/realms_sabrina.jpg"),
    SHELDON_SKAGGS("Sheldon Skaggs", 4, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/realms_sheldon.jpg"),
    SIEGE_TOWER("Siege Tower", 6, 1, Type.SIEGE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/realms_siege_tower.jpg"),
    SIEGFRIED_OF_DENESLE("Siegfried of Denesle", 5, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/realms_siegfried.jpg"),
    SIGISMUND_DIJKSTRA("Sigismund Dijkstra", 4, 1, Type.CLOSE_COMBAT_UNIT, new Spy(), Description.NOTHING, false, "/gwentImages/img/lg/realms_dijkstra.jpg"),
    SILE_DE_TANSARVILLE("Síle de Tansarville", 5, 1, Type.RANGED_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/realms_sheala.jpg"),
    THALER("Thaler", 1, 1, Type.SIEGE_UNIT, new Spy(), Description.NOTHING, false, "/gwentImages/img/lg/realms_thaler.jpg"),
    TREBUCHET("Trebuchet", 6, 2, Type.SIEGE_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/realms_trebuchet.jpg"),
    VERNON_ROCHE("Vernon Roche", 10, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/realms_vernon.jpg"),
    VES("Ves", 5, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/realms_ves.jpg"),
    YARPEN_ZIGRIN("Yarpen Zirgrin", 2, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/realms_yarpen.jpg");

    private final String IMAGE_PATH;
    private final String NAME;
    private final int POWER;
    private final int NO_OF_CARDS_IN_GAME;
    private final Type TYPE;
    private final Ability ABILITY;
    private final Description DESCRIPTION;
    private final boolean IS_HERO;

    RealmsNorthernCards(String name, int power, int noOfCardsInGame, Type type, Ability ability, Description description, boolean isHero, String imagePath) {
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
