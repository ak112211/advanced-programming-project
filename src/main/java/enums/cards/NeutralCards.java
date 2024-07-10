package enums.cards;

import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import model.abilities.Ability;
import model.abilities.ejectabilities.SummonAvenger;
import model.abilities.instantaneousabilities.*;
import model.abilities.persistentabilities.CommandersHorn;
import model.abilities.persistentabilities.MoraleBoost;
import model.abilities.persistentabilities.Weather;
import model.card.Card;

public enum NeutralCards implements CardEnum {
    BITING_FROST("Biting Frost", 0, 3, Type.WEATHER, new Weather(Type.CLOSE_COMBAT_UNIT), Description.SETS_CLOSE_COMBAT_TO_ONE, false, "/gwentImages/img/lg/weather_frost.jpg"),
    IMPENETRABLE_FOG("Impenetrable Fog", 0, 3, Type.WEATHER, new Weather(Type.RANGED_UNIT), Description.SETS_RANGED_TO_ONE, false, "/gwentImages/img/lg/weather_fog.jpg"),
    TORRENTIAL_RAIN("Torrential Rain", 0, 3, Type.WEATHER, new Weather(Type.SIEGE_UNIT), Description.SETS_SIEGE_TO_ONE, false, "/gwentImages/img/lg/weather_rain.jpg"),
    SKELLIGE_STORM("Skellige Storm", 0, 3, Type.WEATHER, new Weather(Type.AGILE_UNIT), Description.SETS_SIEGE_AND_RANGED_TO_ONE, false, "/gwentImages/img/lg/weather_storm.jpg"),
    CLEAR_WEATHER("Clear Weather", 0, 3, Type.WEATHER, new ClearWeather(), Description.CANCELS_ALL_WEATHER_CARDS, false, "/gwentImages/img/lg/weather_clear.jpg"),
    SCORCH("Scorch", 0, 3, Type.SPELL, new Scorch(null), Description.REMOVES_MAX_POWER_CARDS, false, "/gwentImages/img/lg/special_scorch.jpg"),
    COMMANDERS_HORN("Commander’s Horn", 0, 3, Type.SPELL, new CommandersHorn(), Description.DOUBLES_POWER_IN_ROW, false, "/gwentImages/img/lg/special_horn.jpg"),
    DECOY("Decoy", 0, 3, Type.DECOY, new Decoy(), Description.DECOY_CARD, false, "/gwentImages/img/lg/special_decoy.jpg"),
    DANDELION("Dandelion", 2, 1, Type.CLOSE_COMBAT_UNIT, new CommandersHorn(), Description.NOTHING, false, "/gwentImages/img/lg/neutral_dandelion.jpg"),
    BOVINE_DEFENCE_FORCE("Bovine Defence Force", 8, 0, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/neutral_chort.jpg"),
    COW("Cow", 0, 1, Type.RANGED_UNIT, new SummonAvenger(NeutralCards.BOVINE_DEFENCE_FORCE), Description.BECOMES_STRONGER_NEXT_ROUND, false, "/gwentImages/img/lg/neutral_cow.jpg"),
    EMIEL_REGIS("Emiel Regis", 5, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/neutral_emiel.jpg"),
    GAUNTER_ODIMM("Gaunter O’Dimm", 2, 1, Type.SIEGE_UNIT, new Muster("Gaunter O’Dimm Darkness"), Description.NOTHING, false, "/gwentImages/img/lg/neutral_gaunter_odimm.jpg"),
    GAUNTER_ODIMM_DARKNESS("Gaunter O’Dimm Darkness", 4, 3, Type.RANGED_UNIT, new Muster(), Description.NOTHING, false, "/gwentImages/img/lg/neutral_gaunter_odimm_darkness.jpg"),
    GERALT_OF_RIVIA("Geralt of Rivia", 15, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/neutral_geralt.jpg"),
    MYSTERIOUS_ELF("Mysterious Elf", 0, 1, Type.CLOSE_COMBAT_UNIT, new Spy(), Description.NOTHING, true, "/gwentImages/img/lg/neutral_mysterious_elf.jpg"),
    OLGIERD_VON_EVEREC("Olgierd Von Everec", 6, 1, Type.AGILE_UNIT, new MoraleBoost(), Description.NOTHING, false, "/gwentImages/img/lg/neutral_olgierd.jpg"),
    TRISS_MERIGOLD("Triss Merigold", 7, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, true, "/gwentImages/img/lg/neutral_triss.jpg"),
    VESEMIR("Vesemir", 6, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/neutral_vesemir.jpg"),
    VILLENTRETENMERTH("Villentretenmerth", 7, 1, Type.CLOSE_COMBAT_UNIT, new Scorch(Type.CLOSE_COMBAT_UNIT), Description.SCOTCH_ON_ENEMY_CLOSE_COMBAT, false, "/gwentImages/img/lg/neutral_villen.jpg"),
    YENNEFER_OF_VENGERBERG("Yennefer of Vengerberg", 7, 1, Type.RANGED_UNIT, new Medic(), Description.NOTHING, true, "/gwentImages/img/lg/neutral_yennefer.jpg"),
    ZOLTAN_CHIVAY("Zoltan Chivay", 5, 1, Type.CLOSE_COMBAT_UNIT, null, Description.NOTHING, false, "/gwentImages/img/lg/neutral_zoltan.jpg");

    private final String name;
    private final int power;
    private final int noOfCardsInGame;
    private final Type type;
    private final Ability ability;
    private final Description description;
    private final boolean isHero;
    private final String imagePath;

    NeutralCards(String name, int power, int noOfCardsInGame, Type type, Ability ability, Description description, boolean isHero, String imagePath) {
        this.name = name;
        this.power = power;
        this.noOfCardsInGame = noOfCardsInGame;
        this.type = type;
        this.ability = ability;
        this.description = description;
        this.isHero = isHero;
        this.imagePath = imagePath;
    }

    public Card getCard() {
        return new Card(name, type, noOfCardsInGame, power, ability, isHero, Faction.NEUTRAL, description, imagePath, this);
    }

    public int getNoOfCardsInGame() {
        return noOfCardsInGame;
    }
}
