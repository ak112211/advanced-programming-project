package model.abilities;

import enums.cards.CardEnum;
import model.card.Card;

import java.util.ArrayList;

public class Berserker extends Ability {
    private final CardEnum newCardEnum;

    public Berserker(CardEnum newCardEnum) {
        this.newCardEnum = newCardEnum;
        setIconName("berserker");
    }

    public void affect(ArrayList<Card> inGameCards, Card myCard) {
        int i = inGameCards.indexOf(myCard);
        Card newCard = newCardEnum.getCard();
        newCard.setDefaultRow(myCard.getRow().isPlayer1());
        inGameCards.set(i, newCard);
    }

}