package model.abilities.instantaneousabilities;

import enums.cardsinformation.CardsPlace;
import model.Game;
import model.card.Card;

import java.util.ArrayList;
import java.util.Optional;

public class GetCard extends InstantaneousAbility {
    private final CardsPlace CARDS_PLACE;
    private final boolean FROM_OWN;
    private final int AMOUNT;
    private final boolean RANDOM;

    public GetCard(CardsPlace cardsPlace, boolean fromOwn, int amount, boolean random) {
        CARDS_PLACE = cardsPlace;
        FROM_OWN = fromOwn;
        AMOUNT = amount;
        RANDOM = random;
    }

    public void affect(Game game, Card myCard) {
        ArrayList<Card> cardList = CARDS_PLACE.getCards(game, game.isPlayer1Turn() == FROM_OWN);
        for (int i = 0; i < AMOUNT; i++) {
            Optional<Card> card = game.chooseCard(cardList, true, RANDOM);
            if (card.isEmpty()) {
                return;
            }
            game.moveCard(card.get(), cardList,
                    CardsPlace.IN_HAND.getPlayerCards(game));
        }
    }
}
