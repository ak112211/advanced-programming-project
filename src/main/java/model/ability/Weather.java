package model.ability;

import enums.Row;
import model.Game;
import model.card.Card;

public class Weather extends PersistentAbility {
    /**
     * @param row
     * @param game
     * @param card
     */
    public Weather(Row row, Game game, Card card) {
        super(row, game, card);
    }
}