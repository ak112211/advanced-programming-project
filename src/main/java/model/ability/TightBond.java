package model.ability;

import enums.Row;
import model.Game;
import model.card.Card;

public class TightBond extends PersistentAbility {
    /**
     * @param row
     * @param game
     * @param card
     */
    public TightBond(Row row, Game game, Card card) {
        super(row, game, card);
    }
}