package model.card;

import enums.Row;
import enums.cardsinformation.*;
import model.abilities.Ability;

import java.awt.*;

public class Card extends Rectangle {
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

    private String imagePath;

    public Card(String name, Type type, int noOfCardsInGame, int power, Ability ability, boolean isHero,
                Faction faction, Description description, String imagePath) {
        this.NAME = name;
        this.TYPE = type;
        this.NO_OF_CARDS_IN_GAME = noOfCardsInGame;
        this.FIRST_POWER = power;
        this.power = power;
        ability.setCard(this);
        this.ABILITY = ability;
        this.IS_HERO = isHero;
        this.imagePath = imagePath;
        this.FACTION = faction;
        this.DESCRIPTION = description;
    }

    public String getName() {
        return NAME;
    }

    public Type getType() {
        return TYPE;
    }

    public int getNoOfCardsInGame() {
        return NO_OF_CARDS_IN_GAME;
    }

    public Ability getAbility() {
        return ABILITY;
    }

    public Faction getFaction() {
        return FACTION;
    }

    public Description getDescription() {
        return DESCRIPTION;
    }

    public int getFirstPower() {
        return FIRST_POWER;
    }

    public boolean isHero() {
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
