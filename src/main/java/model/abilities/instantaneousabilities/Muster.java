package model.abilities.instantaneousabilities;

import model.Game;
import model.card.Card;

import java.util.Arrays;
import java.util.List;

public class Muster extends InstantaneousAbility {
    private final List<String> cardNames;

    public Muster(Card... cards) {
        this.cardNames = Arrays.stream(cards).map(Card::getName).toList();
    }

    private boolean canMuster(Card card) {
        return !(sameName(getCard(), card) || cardNames.contains(card.getName()));
    }

    public void affect(Game game) {
        if (game.isPlayer1Turn()) {
            for (int i = 0; i < game.getPlayer1Deck().size(); ) {
                Card card = game.getPlayer1Deck().get(i);
                if (canMuster(card)) {
                    card.setRow(card.getType().getRow(true));
                    game.moveCard(card, game.getPlayer1Deck(), game.getInGameCards());
                } else {
                    i++;
                }
            }
            for (int i = 0; i < game.getPlayer1InHandCards().size(); ) {
                Card card = game.getPlayer1InHandCards().get(i);
                if (canMuster(card)) {
                    game.player1PlayCard(card, null);
                } else {
                    i++;
                }
            }
        } else {
            for (int i = 0; i < game.getPlayer2Deck().size(); ) {
                Card card = game.getPlayer2Deck().get(i);
                if (canMuster(card)) {
                    card.setRow(card.getType().getRow(false));
                    game.moveCard(card, game.getPlayer2Deck(), game.getInGameCards());
                } else {
                    i++;
                }
            }
            for (int i = 0; i < game.getPlayer2InHandCards().size(); ) {
                Card card = game.getPlayer2InHandCards().get(i);
                if (canMuster(card)) {
                    game.player2PlayCard(card, null);
                } else {
                    i++;
                }
            }
        }
    }
}