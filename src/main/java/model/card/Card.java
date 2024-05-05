package model.card;

import enums.Row;
import enums.cardsinformation.*;
import model.abilities.Ability;

public class Card {
    private final String NAME;
    private final Type TYPE;
    private final int NO_OF_CARDS_IN_GAME;
    private final int FIRST_POWER;
    private int power;

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
        this.FIRST_POWER = power;
        this.power = power;
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

    public int getFIRST_POWER() {
        return FIRST_POWER;
    }

    public boolean getIS_HERO() {
        return IS_HERO;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

}
