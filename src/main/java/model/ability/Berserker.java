package model.ability;

import enums.Row;
import model.Game;
import model.card.Card;

public class Berserker extends Ability {
    /**
     * @param row
     * @param game
     * @param card
     */
    public Berserker(Row row, Game game, Card card) {
        super(row, game, card);
    }
}