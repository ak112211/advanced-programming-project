package model.abilities.instantaneousabilities;

import enums.cardsinformation.CardsPlace;
import model.Game;
import model.card.Card;

import java.util.ArrayList;
import java.util.Optional;

public class KillAndGetCard extends InstantaneousAbility {
    private final int KILL_AMOUNT;

    public KillAndGetCard(int killAmount) {
        KILL_AMOUNT = killAmount;
    }

    public void affect(Game game, Card myCard) {
        ArrayList<Card> inHandCards = CardsPlace.IN_HAND.getPlayerCards(game);
        ArrayList<Card> graveyard = CardsPlace.GRAVEYARD.getPlayerCards(game);
        if (inHandCards.size() < KILL_AMOUNT) {
            return;
        }
        for (int i = 0; i < KILL_AMOUNT; i++) {
            game.moveCard(game.chooseCard(inHandCards, false).orElseThrow(), inHandCards, graveyard);
        }
        if (game.isPlayer1Turn()) {
            game.player1GetRandomCard();
        } else {
            game.player2GetRandomCard();
        }
    }
}
