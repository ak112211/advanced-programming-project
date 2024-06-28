package model.abilities.instantaneousabilities;

import enums.Row;
import enums.cardsinformation.CardsPlace;
import model.Game;
import model.abilities.passiveabilities.DisruptMedic;
import model.card.Card;

import java.util.ArrayList;
import java.util.Optional;

public class Medic extends InstantaneousAbility {
    public void affect(Game game, Card myCard) {
        ArrayList<Card> graveyard = CardsPlace.GRAVEYARD.getPlayerCards(game);
        Optional<Card> card = game.chooseCard(graveyard, true, DisruptMedic.exists(game));
        if (card.isEmpty()) {
            return;
        }
        Row row = card.get().getType().getRow(game.isPlayer1Turn());
        card.get().setRow(row);
        game.moveCard(card.get(), graveyard, game.getInGameCards());
    }
}