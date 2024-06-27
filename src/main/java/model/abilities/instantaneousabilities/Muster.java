package model.abilities.instantaneousabilities;

import model.Game;
import model.card.Card;

import java.util.Arrays;
import java.util.stream.Stream;

public class Muster extends InstantaneousAbility {
    private final String[] MUSTER_CARDS_NAME;

    public Muster(String... cardNames) {
        MUSTER_CARDS_NAME = cardNames;
    }

    private boolean canMuster(Card card, String myCardName) {
        String cardName = card.getName().split(":")[0];
        return Stream.concat(Arrays.stream(MUSTER_CARDS_NAME), Stream.of(myCardName))
                .anyMatch(name -> name.equals(cardName));
    }

    public void affect(Game game, Card myCard) {
        String myCardName = myCard.getName().split(":")[0];
        if (game.isPlayer1Turn()) {
            for (int i = 0; i < game.getPlayer1Deck().size(); ) {
                Card card = game.getPlayer1Deck().get(i);
                if (canMuster(card, myCardName)) {
                    card.setDefaultRow(true);
                    game.moveCard(card, game.getPlayer1Deck(), game.getInGameCards());
                } else {
                    i++;
                }
            }
            for (int i = 0; i < game.getPlayer1InHandCards().size(); ) {
                Card card = game.getPlayer1InHandCards().get(i);
                if (canMuster(card, myCardName)) {
                    game.player1PlayCard(card, null);
                } else {
                    i++;
                }
            }
        } else {
            for (int i = 0; i < game.getPlayer2Deck().size(); ) {
                Card card = game.getPlayer2Deck().get(i);
                if (canMuster(card, myCardName)) {
                    card.setDefaultRow(false);
                    game.moveCard(card, game.getPlayer2Deck(), game.getInGameCards());
                } else {
                    i++;
                }
            }
            for (int i = 0; i < game.getPlayer2InHandCards().size(); ) {
                Card card = game.getPlayer2InHandCards().get(i);
                if (canMuster(card, myCardName)) {
                    game.player2PlayCard(card, null);
                } else {
                    i++;
                }
            }
        }
    }
}