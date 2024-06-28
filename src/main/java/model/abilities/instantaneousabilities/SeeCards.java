package model.abilities.instantaneousabilities;

import enums.cardsinformation.CardsPlace;
import model.Game;
import model.card.Card;

import java.util.ArrayList;
import java.util.HashSet;

public class SeeCards extends InstantaneousAbility {
    public final int AMOUNT;

    public SeeCards(int amount) {
        AMOUNT = amount;
    }

    public void affect(Game game, Card myCard) {
        ArrayList<Card> enemyInHandCards = CardsPlace.IN_HAND.getCards(game, !game.isPlayer1Turn());
        HashSet<Card> cardsToBeSeen = new HashSet<>();
        while (cardsToBeSeen.size() < AMOUNT && cardsToBeSeen.size() < enemyInHandCards.size()) {
            cardsToBeSeen.add(Game.chooseRandomCard(enemyInHandCards, false).orElseThrow());
        }
        game.chooseCardOrPass(cardsToBeSeen.stream().toList());
    }
}
