package model.abilities.instantaneousabilities;

import enums.cardsinformation.CardsPlace;
import model.Game;
import model.card.Card;

public class ResetGraveyardToDeck extends InstantaneousAbility{
    public void affect(Game game, Card myCard) {
        CardsPlace.DECK.getPlayerCards(game).addAll(CardsPlace.GRAVEYARD.getPlayerCards(game));
        CardsPlace.GRAVEYARD.getPlayerCards(game).clear();
    }
}
