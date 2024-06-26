package model.abilities.instantaneousabilities;

import enums.cardsinformation.CardsPlace;
import model.Game;
import model.card.Card;

import java.util.ArrayList;

public class GetCard extends InstantaneousAbility{
    private final CardsPlace cardsPlace;
    private final boolean fromOwn;
    private final int amount;
    private final boolean random;

    public GetCard(CardsPlace cardsPlace, boolean fromOwn, int amount, boolean random) {
        this.cardsPlace = cardsPlace;
        this.fromOwn = fromOwn;
        this.amount = amount;
        this.random = random;
    }

    public void affect(Game game, Card myCard) {
        ArrayList<Card> cardList = cardsPlace.getCards(game, game.isPlayer1Turn() == fromOwn);
        for (int i = 0; i <amount; i++) {
            game.moveCard(game.chooseCard(cardList, true, random), cardList,
                    CardsPlace.IN_HAND.getPlayerCards(game));
        }
    }
}
