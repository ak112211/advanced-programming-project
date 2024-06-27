package model.abilities;

import enums.cards.CardEnum;
import model.card.Card;

import java.util.ArrayList;

public class Berserker extends Ability {
    private final CardEnum NEW_CARD_ENUM;

    public Berserker(CardEnum newCardEnum) {
        NEW_CARD_ENUM = newCardEnum;
    }

    public void affect(ArrayList<Card> inGameCards, Card myCard) {
        int i = inGameCards.indexOf(myCard);
        Card newCard = NEW_CARD_ENUM.getCard();
        newCard.setDefaultRow(myCard.getRow().isPlayer1());
        inGameCards.set(i, newCard);
    }

}