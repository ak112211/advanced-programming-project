package model.abilities.ejectabilities;

import enums.cards.CardEnum;
import model.Game;
import model.card.Card;

import java.util.ArrayList;

public class SummonAvenger extends EjectAbility {
    private static final ArrayList<Card> cards = new ArrayList<>();
    private final CardEnum newCardEnum;

    public SummonAvenger(CardEnum newCardEnum) {
        this.newCardEnum = newCardEnum;
        setIconName("avenger");
    }

    public void affect(Card myCard) {
        Card newCard = newCardEnum.getCard();
        newCard.setSmallImage();
        newCard.setDefaultRow(myCard.getRow().isPlayer1());
        cards.add(newCard);
    }

    public static void startTurnAffect(Game game) {
        game.getInGameCards().addAll(cards);
        cards.clear();
    }
}