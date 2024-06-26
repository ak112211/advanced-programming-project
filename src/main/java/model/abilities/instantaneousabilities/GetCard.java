package model.abilities.instantaneousabilities;

import enums.cardsinformation.CardsPlace;
import model.Game;
import model.card.Card;

import java.util.ArrayList;

public class GetCard extends InstantaneousAbility{
    CardsPlace cardsPlace;
    boolean fromOwn;
    int amount;
    boolean random;

    public GetCard(CardsPlace cardsPlace, boolean fromOwn, int amount, boolean random) {
        this.cardsPlace = cardsPlace;
        this.fromOwn = fromOwn;
        this.amount = amount;
        this.random = random;
    }

    public void affect(Game game, Card myCard) {
        ArrayList<Card> cards = cardsPlace.getCards(game, game.isPlayer1Turn() ^ fromOwn);
        for (int i = 0; i <amount; i++) {
            game.moveCard(game.chooseCard(cards, true), cards,
                    CardsPlace.IN_HAND.getCards(game,game.isPlayer1Turn()));
        }
    }
}
