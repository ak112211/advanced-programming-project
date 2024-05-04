package model.ability;

import enums.Row;
import model.Game;
import model.ability.Ability;
import model.card.Card;

public abstract class EjectAbility extends Ability {
    /**
     * @param row
     * @param game
     * @param card
     */
    public EjectAbility(Row row, Game game, Card card) {
        super(row, game, card);
    }
}