package model.ability;

import enums.Row;
import model.Game;
import model.card.Card;

public class Spy extends InstantaneousAbility {
    /**
     * @param row
     * @param game
     * @param card
     */
    public Spy(Row row, Game game, Card card) {
        super(row, game, card);
    }
}