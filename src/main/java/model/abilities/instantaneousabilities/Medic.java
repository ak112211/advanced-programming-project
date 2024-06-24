package model.abilities.instantaneousabilities;

import enums.Row;
import enums.cardsinformation.CardsPlace;
import model.Game;
import model.card.Card;

public class Medic extends InstantaneousAbility {
    public void affect(Game game) {
        Card card = game.chooseCard(CardsPlace.GRAVEYARD.getCards(game, game.isPlayer1Turn()), true);
        Row row = card.getType().getRow(game.isPlayer1Turn());
        card.setRow(row);
        game.moveCard(card,
                getCard().getRow().isPlayer1() ? game.getPlayer1GraveyardCards() : game.getPlayer2GraveyardCards(),
                game.getInGameCards());
    }
}