package model.card;

import enums.Row;
import enums.cards.*;
import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import javafx.scene.input.DataFormat;
import model.App;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.Spy;
import view.GamePaneController;
import view.cardpane.CardIcon;
import view.cardpane.CardPane;

public class Card extends CardPane {
    public static final DataFormat DATA_FORMAT = new DataFormat("model.card.Card");

    private final String name;
    private final Type type;
    private final int noOfCardsInGame;
    private final int firstPower;
    private int power;
    private Row row;
    private final boolean isHero;
    private final Ability ability;
    private final Faction faction;
    private final Description description;
    private final CardEnum cardEnum;

    public Card(String name, Type type, int noOfCardsInGame, int power, Ability ability, boolean isHero, Faction faction, Description description, String imagePath, CardEnum cardEnum) {
        super(imagePath, new CardIcon(ability != null ? ability.getIconName() : null, type, isHero));
        this.name = name;
        this.type = type;
        this.noOfCardsInGame = noOfCardsInGame;
        this.firstPower = power;
        this.power = power;
        this.isHero = isHero;
        this.faction = faction;
        this.description = description;
        this.cardEnum = cardEnum;
        this.ability = ability;
        if (App.getCurrentController() instanceof GamePaneController) {
            setSmallImage();
        } else {
            setBigImage();
        }
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

    public boolean isSpecial() {
        return type.isSpecial();
    }

    public void setPowerText() {
        if (!type.isSpecial()) {
            setPowerText(power, firstPower);
        }
    }

    public static Card getCardFromSaved(String type, int power, Row row) {
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
