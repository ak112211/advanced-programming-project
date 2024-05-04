package model.card;

import enums.Row;
import enums.cardsinformation.*;
import model.abilities.Ability;

public class Card {
    private final String NAME;
    private final Type TYPE;
    private final int NO_OF_CARDS_IN_GAME;
    private final int POWER;
    private int changedPower;

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    private Row row;
    private final Ability ABILITY;
    private final boolean IS_HERO;
    private final Faction FACTION;
    private final Description DESCRIPTION;

    public Card(String name, Type type, int noOfCardsInGame, int power, Ability ability, boolean isHero,
                Faction faction, Description description) {
        this.NAME = name;
        this.TYPE = type;
        this.NO_OF_CARDS_IN_GAME = noOfCardsInGame;
        this.POWER = power;
        ability.setCard(this);
        this.ABILITY = ability;
        this.IS_HERO = isHero;
        this.FACTION = faction;
        this.DESCRIPTION = description;
    }

    public String getNAME() {
        return NAME;
    }

    public Type getTYPE() {
        return TYPE;
    }

    public int getNO_OF_CARDS_IN_GAME() {
        return NO_OF_CARDS_IN_GAME;
    }

    public Ability getABILITY() {
        return ABILITY;
    }

    public Faction getFACTION() {
        return FACTION;
    }

    public Description getDESCRIPTION() {
        return DESCRIPTION;
    }

    public int getPOWER() {
        return POWER;
    }

    public boolean getIS_HERO() {
        return IS_HERO;
    }

    public int getChangedPower() {
        return changedPower;
    }

    public void setChangedPower(int power) {
        this.changedPower = power;
    }

}
