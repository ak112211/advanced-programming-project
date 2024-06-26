package enums.cardsinformation;

import enums.Row;
import model.Game;
import model.card.Card;

import java.util.ArrayList;

public enum CardsPlace {
    IN_GAME, IN_HAND, DECK, GRAVEYARD;

    public ArrayList<Card> getCards(Game game, boolean isPayer1) {
        if (isPayer1) {
            return switch (this) {
                case IN_GAME -> game.getInGameCards();
                case IN_HAND -> game.getPlayer1InHandCards();
                case DECK -> game.getPlayer1Deck();
                case GRAVEYARD -> game.getPlayer1GraveyardCards();
            };
        } else {
            return switch (this) {
                case IN_GAME -> game.getInGameCards();
                case IN_HAND -> game.getPlayer2InHandCards();
                case DECK -> game.getPlayer2Deck();
                case GRAVEYARD -> game.getPlayer2GraveyardCards();
            };
        }
    }

    public ArrayList<Card> getPlayerCards(Game game) {
        return getCards(game, game.isPlayer1Turn());
    }
}
