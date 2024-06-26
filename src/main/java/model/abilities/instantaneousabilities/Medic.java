package model.abilities.instantaneousabilities;

import enums.Row;
import enums.cardsinformation.CardsPlace;
import model.Game;
import model.abilities.passiveabilities.DisruptMedic;
import model.card.Card;

import java.util.ArrayList;

public class Medic extends InstantaneousAbility {
    public void affect(Game game, Card myCard) {
        ArrayList<Card> graveyard = CardsPlace.GRAVEYARD.getPlayerCards(game);
        Card card = game.chooseCard(graveyard, true, DisruptMedic.exists(game));
        Row row = card.getType().getRow(game.isPlayer1Turn());
        card.setRow(row);
        game.moveCard(card, graveyard, game.getInGameCards());
    }
}