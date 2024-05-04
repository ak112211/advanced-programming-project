package model.ability;

import enums.Row;
import model.Game;
import model.card.Card;

public class CommandersHorn extends PersistentAbility {
    /**
     * @param row
     * @param game
     * @param card
     */
    public CommandersHorn(Row row, Game game, Card card) {
        super(row, game, card);
    }
}