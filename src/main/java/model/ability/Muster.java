package model.ability;

import enums.Row;
import model.Game;
import model.card.Card;

public class Muster extends InstantaneousAbility {
    /**
     * @param row
     * @param game
     * @param card
     */
    public Muster(Row row, Game game, Card card) {
        super(row, game, card);
    }
}