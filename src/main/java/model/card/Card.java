package model.card;

import enums.cards.CardEnum;
import javafx.scene.shape.Rectangle;
import enums.Row;
import enums.cardsinformation.*;
import javafx.scene.input.DataFormat;
import javafx.scene.paint.ImagePattern;
import model.abilities.Ability;

import java.awt.*;

public class Card extends Rectangle {
    public static final DataFormat DATA_FORMAT = new DataFormat("model.card.Card");

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
    private final String IMAGE_PATH;
    private final CardEnum CARDE_NUM;

    public Card(String name, Type type, int noOfCardsInGame, int power, Ability ability, boolean isHero, Faction faction, Description description, String imagePath, CardEnum cardEnum) {
        this.NAME = name;
        this.TYPE = type;
        this.NO_OF_CARDS_IN_GAME = noOfCardsInGame;
        this.FIRST_POWER = power;
        this.power = power;
        this.ABILITY = ability;
        this.IS_HERO = isHero;
        this.FACTION = faction;
        this.DESCRIPTION = description;
        this.IMAGE_PATH = imagePath;
        this.CARDE_NUM = cardEnum;

        this.setWidth(70);
        this.setHeight(100);
        this.setFill(new ImagePattern(new javafx.scene.image.Image(imagePath)));
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

    public String getImagePath() {
        return IMAGE_PATH;
    }

    public CardEnum getCardEnum() {
        return CARDE_NUM;
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

    public void setDefaultRow(boolean isPlayer1Turn) {
        row = TYPE.getRow(isPlayer1Turn);
    }
}
