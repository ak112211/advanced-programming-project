package model.abilities.ejectabilities;

import enums.cards.CardEnum;
import model.card.Card;

import java.util.ArrayList;

public class SummonAvenger extends EjectAbility {
    private static final ArrayList<Card> cardList = new ArrayList<>();
    private final CardEnum newCardEnum;

    public SummonAvenger(CardEnum newCardEnum) {
        this.newCardEnum = newCardEnum;
    }

    public void Affect(Card myCard) {
        Card newCard = newCardEnum.getCard();
        newCard.setDefaultRow(myCard.getRow().isPlayer1());
        cardList.add(newCard);
    }
}