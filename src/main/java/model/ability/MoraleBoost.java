package model.ability;

import enums.Row;
import model.Game;
import model.card.Card;

public class MoraleBoost extends PersistentAbility {
    /**
     * @param row
     * @param game
     * @param card
     */
    public MoraleBoost(Row row, Game game, Card card) {
        super(row, game, card);
    }
}