package model.card;

import enums.cards.*;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import enums.Row;
import enums.cardsinformation.*;
import javafx.scene.input.DataFormat;
import javafx.scene.paint.ImagePattern;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.Spy;

import java.util.Objects;

public class Card extends Rectangle {
    public static final DataFormat DATA_FORMAT = new DataFormat("model.card.Card");

    private final String name;
    private final Type type;
    private final int noOfCardsInGame;
    private final int firstPower;
    private int power;
    private Row row;
    private final Ability ability;
    private final boolean isHero;
    private final Faction faction;
    private final Description description;
    private final String imagePath;
    private final CardEnum cardEnum;

    public Card(String name, Type type, int noOfCardsInGame, int power, Ability ability, boolean isHero, Faction faction, Description description, String imagePath, CardEnum cardEnum) {
        this.name = name;
        this.type = type;
        this.noOfCardsInGame = noOfCardsInGame;
        firstPower = power;
        this.power = power;
        this.ability = ability;
        this.isHero = isHero;
        this.faction = faction;
        this.description = description;
        this.imagePath = imagePath;
        this.cardEnum = cardEnum;

        setBigImage();
    }

    public void setSmallImage() {
        this.setArcWidth(5);
        this.setArcHeight(5);
        this.setWidth(53);
        this.setHeight(79);
        try {
            this.setFill(new ImagePattern(new Image(
                    Objects.requireNonNull(getClass().getResource(imagePath.replaceFirst("/lg/", "/sm/")))
                            .toExternalForm())));
        } catch (RuntimeException e){
            System.out.println("couldn't find " + imagePath.replaceFirst("/lg/", "/sm/"));
        }

    }

    public void setBigImage() {
        this.setArcWidth(10);
        this.setArcHeight(10);
        this.setWidth(70);
        this.setHeight(100);
        this.setFill(new ImagePattern(new Image(
                Objects.requireNonNull(getClass().getResource(imagePath))
                        .toExternalForm())));
    }

    public Rectangle getBigRectangle() {
        Rectangle rectangle = new Rectangle();
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        rectangle.setWidth(70);
        rectangle.setHeight(100);
        rectangle.setFill(new ImagePattern(new Image(
                Objects.requireNonNull(getClass().getResource(imagePath))
                        .toExternalForm())));
        return rectangle;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getNoOfCardsInGame() {
        return noOfCardsInGame;
    }

    public Ability getAbility() {
        return ability;
    }

    public Faction getFaction() {
        return faction;
    }

    public Description getDescription() {
        return description;
    }

    public int getFirstPower() {
        return firstPower;
    }

    public boolean isHero() {
        return isHero;
    }

    public String getImagePath() {
        return imagePath;
    }

    public CardEnum getCardEnum() {
        return cardEnum;
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

    public Row getDefaultRow(boolean isPlayer1Turn) {
        return type.getRow(isPlayer1Turn ^ ability instanceof Spy);
    }

    public void setDefaultRow(boolean isPlayer1Turn) {
        row = getDefaultRow(isPlayer1Turn);
    }

    public boolean same(Card card) {
        return cardEnum.equals(card.cardEnum);
    }

    public boolean sameRow(Card card) {
        return row.equals(card.row);
    }


    public static Card getCardFromType(String type, int power, Row row) {
        for (RealmsNorthernCards cardEnum : RealmsNorthernCards.values()) {
            if (cardEnum.toString().equals(type)) {
                Card card = cardEnum.getCard();
                card.setRow(row);
                card.setPower(power);
                return card;
            }
        }
        for (ScoiaTaelCards cardEnum : ScoiaTaelCards.values()) {
            if (cardEnum.toString().equals(type)) {
                Card card = cardEnum.getCard();
                card.setRow(row);
                card.setPower(power);
                return card;
            }
        }
        for (MonstersCards cardEnum : MonstersCards.values()) {
            if (cardEnum.toString().equals(type)) {
                Card card = cardEnum.getCard();
                card.setRow(row);
                card.setPower(power);
                return card;
            }
        }
        for (EmpireNilfgaardianCards cardEnum : EmpireNilfgaardianCards.values()) {
            if (cardEnum.toString().equals(type)) {
                Card card = cardEnum.getCard();
                card.setRow(row);
                card.setPower(power);
                return card;
            }
        }
        for (SkelligeCards cardEnum : SkelligeCards.values()) {
            if (cardEnum.toString().equals(type)) {
                Card card = cardEnum.getCard();
                card.setRow(row);
                card.setPower(power);
                return card;
            }
        }
        for (NeutralCards cardEnum : NeutralCards.values()) {
            if (cardEnum.toString().equals(type)) {
                Card card = cardEnum.getCard();
                card.setRow(row);
                card.setPower(power);
                return card;
            }
        }
        return null;
    }
}
