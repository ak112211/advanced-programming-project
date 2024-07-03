package model.abilities.ejectabilities;

import enums.cards.CardEnum;
import model.card.Card;

import java.util.ArrayList;

public class SummonAvenger extends EjectAbility {
    private static final ArrayList<Card> CARDS = new ArrayList<>();
    private final CardEnum NEW_CARD_ENUM;

    public SummonAvenger(CardEnum newCardEnum) {
        this.NEW_CARD_ENUM = newCardEnum;
        setIconName("avenger");
    }

    public void Affect(Card myCard) {
        Card newCard = NEW_CARD_ENUM.getCard();
        newCard.setDefaultRow(myCard.getRow().isPlayer1());
        CARDS.add(newCard);
    }
}