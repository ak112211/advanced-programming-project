package model.abilities.ejectabilities;

import enums.cards.CardEnum;
import model.Game;
import model.card.Card;

import java.util.ArrayList;

public class SummonAvenger extends EjectAbility {
    private static final ArrayList<Card> cards = new ArrayList<>();
    private final CardEnum NEW_CARD_ENUM;

    public SummonAvenger(CardEnum newCardEnum) {
        this.NEW_CARD_ENUM = newCardEnum;
        setIconName("avenger");
    }

    public void affect(Card myCard) {
        Card newCard = NEW_CARD_ENUM.getCard();
        newCard.setSmallImage();
        newCard.setDefaultRow(myCard.getRow().isPlayer1());
        cards.add(newCard);
    }

    public static void startTurnAffect(Game game) {
        game.getInGameCards().addAll(cards);
        cards.clear();
    }
}