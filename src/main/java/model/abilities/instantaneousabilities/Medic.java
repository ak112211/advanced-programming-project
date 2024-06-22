package model.abilities.instantaneousabilities;

import enums.Row;
import model.Game;
import model.card.Card;

public class Medic extends InstantaneousAbility {
    public void affect(Game game, Card card, Row row) {
        if (row == null) {
            row = card.getType().getRow(getCard().getRow().isPlayer1());
        }
        card.setRow(row);
        game.moveCard(card,
                getCard().getRow().isPlayer1() ? game.getPlayer1GraveyardCards() : game.getPlayer2GraveyardCards(),
                game.getInGameCards());
    }
}