package model.abilities.instantaneousabilities;

import enums.Row;
import model.Game;
import model.card.Card;

public class Medic extends InstantaneousAbility {
    public void affect(Game game, Card card, Row row){
        card.setRow(row);
        game.moveCard(card,
                row.isPlayer1() ? game.getPlayer1GraveyardCards() : game.getPlayer2GraveyardCards(),
                game.getInGameCards());
    }
}