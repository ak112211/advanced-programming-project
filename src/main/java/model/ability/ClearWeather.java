package model.ability;

import enums.Row;
import model.Game;
import model.card.Card;

public class ClearWeather extends InstantaneousAbility {
    /**
     * @param row
     * @param game
     * @param card
     */
    public ClearWeather(Row row, Game game, Card card) {
        super(row, game, card);
    }
}