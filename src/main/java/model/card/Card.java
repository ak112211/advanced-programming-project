package model.card;

import enums.cards.*;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import enums.Row;
import enums.cardsinformation.*;
import javafx.scene.input.DataFormat;
import javafx.scene.paint.ImagePattern;
import model.abilities.Ability;

import java.util.Objects;

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
    private final CardEnum CARD_ENUM;

    public Card(String name, Type type, int noOfCardsInGame, int power, Ability ability, boolean isHero, Faction faction, Description description, String imagePath, CardEnum cardEnum) {
        NAME = name;
        TYPE = type;
        NO_OF_CARDS_IN_GAME = noOfCardsInGame;
        FIRST_POWER = power;
        this.power = power;
        ABILITY = ability;
        IS_HERO = isHero;
        FACTION = faction;
        DESCRIPTION = description;
        IMAGE_PATH = imagePath;
        CARD_ENUM = cardEnum;

        setBigImage();
    }

    public void setSmallImage() {
        this.setArcWidth(5);
        this.setArcHeight(5);
        this.setWidth(53);
        this.setHeight(79);
        try {
            this.setFill(new ImagePattern(new Image(
                    Objects.requireNonNull(getClass().getResource(IMAGE_PATH.replaceFirst("/lg/", "/sm/")))
                            .toExternalForm())));
        } catch (RuntimeException e){
            System.out.println("couldn't find " + IMAGE_PATH.replaceFirst("/lg/", "/sm/"));
        }

    }

    public void setBigImage() {
        this.setArcWidth(10);
        this.setArcHeight(10);
        this.setWidth(70);
        this.setHeight(100);
        this.setFill(new ImagePattern(new Image(
                Objects.requireNonNull(getClass().getResource(IMAGE_PATH))
                        .toExternalForm())));
    }

    public Rectangle getBigRectangle() {
        Rectangle rectangle = new Rectangle();
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        rectangle.setWidth(70);
        rectangle.setHeight(100);
        rectangle.setFill(new ImagePattern(new Image(
                Objects.requireNonNull(getClass().getResource(IMAGE_PATH))
                        .toExternalForm())));
        return rectangle;
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
        return CARD_ENUM;
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

    public boolean same(Card card) {
        return CARD_ENUM.equals(card.CARD_ENUM);
    }

    public boolean sameRow(Card card) {
        return row.equals(card.row);
    }
    
    public static Card getCardFromType(String type) {
        for (RealmsNorthernCards cardEnum : RealmsNorthernCards.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getCard();
            }
        }
        for (ScoiaTaelCards cardEnum : ScoiaTaelCards.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getCard();
            }        }
        for (MonstersCards cardEnum : MonstersCards.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getCard();
            }        }
        for (EmpireNilfgaardianCards cardEnum : EmpireNilfgaardianCards.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getCard();
            }        }
        for (SkelligeCards cardEnum : SkelligeCards.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getCard();
            }        }
        for (NeutralCards cardEnum : NeutralCards.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getCard();
            }
        }
        return null;
    }
}
