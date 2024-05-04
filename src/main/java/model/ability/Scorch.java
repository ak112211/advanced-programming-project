package model.ability;

import enums.Row;
import model.Game;
import model.card.Card;

public class Scorch extends InstantaneousAbility {
    /**
     * @param row
     * @param game
     * @param card
     */
    public Scorch(Row row, Game game, Card card) {
        super(row, game, card);
    }
}