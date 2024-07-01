package model.abilities.instantaneousabilities;

import enums.Row;
import enums.cardsinformation.CardsPlace;
import model.Game;
import model.abilities.passiveabilities.DisruptMedic;
import model.card.Card;

import java.util.ArrayList;
import java.util.Optional;

public class Medic extends InstantaneousAbility {
    public Medic() {
        setIconPath("medic");
    }
    public void affect(Game game, Card myCard) {
        ArrayList<Card> graveyard = CardsPlace.GRAVEYARD.getPlayerCards(game);
        Optional<Card> card = game.chooseCard(graveyard, true, DisruptMedic.exists(game));
        if (card.isEmpty()) {
            return;
        }
        card.get().setDefaultRow(game.isPlayer1Turn());
        game.moveCard(card.get(), graveyard, game.getInGameCards());
    }
}