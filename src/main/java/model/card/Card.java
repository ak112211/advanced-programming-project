package model.card;

import enums.cardsinformation.*;

public class Card {
    private final String NAME;
    private final Type TYPE;
    private final int NO_OF_CARDS_IN_GAME;
    private int power;
    private final Ability ABILITY;
    private final Faction FACTION;
    private final Description DESCRIPTION;

    public Card(String name, Type type, int noOfCardsInGame, int power, Ability ability,
                Faction faction, Description description) {
        this.NAME = name;
        this.TYPE = type;
        this.NO_OF_CARDS_IN_GAME = noOfCardsInGame;
        this.power = power;
        this.ABILITY = ability;
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

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
