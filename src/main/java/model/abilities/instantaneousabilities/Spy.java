package model.abilities.instantaneousabilities;

import enums.cardsinformation.CardsPlace;
import model.Game;
import model.card.Card;

import java.util.ArrayList;
import java.util.Random;

public class Spy extends InstantaneousAbility {
    public void affect(Game game, Card myCard) {
        ArrayList<Card> deck = CardsPlace.DECK.getCards(game, game.isPlayer1Turn());
        ArrayList<Card> inHandCards = CardsPlace.IN_HAND.getCards(game, game.isPlayer1Turn());
        Random rand = new Random();
        for (int i = 0; i < 2; i++) {
            Card card = deck.get(rand.nextInt(deck.size()));
            game.moveCard(card, deck, inHandCards);
        }
    }
}