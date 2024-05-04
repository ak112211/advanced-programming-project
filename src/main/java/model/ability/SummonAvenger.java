package model.ability;

import enums.Row;
import model.Game;
import model.card.Card;

public class SummonAvenger extends EjectAbility {
    /**
     * @param row
     * @param game
     * @param card
     */
    public SummonAvenger(Row row, Game game, Card card) {
        super(row, game, card);
    }
}