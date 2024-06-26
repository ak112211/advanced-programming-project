package model.abilities.openingabilities;

import enums.cardsinformation.CardsPlace;
import model.Game;
import model.abilities.passiveabilities.CancelLeaderAbility;
import model.abilities.passiveabilities.PassiveAbility;
import model.card.Card;

import java.util.ArrayList;

public class GetACardAtStart extends OpeningAbility {
    public void affect(Game game, boolean isPlayer1) {
        if (CancelLeaderAbility.exists(game)) {
            return;
        }
        ArrayList<Card> inHandCards = CardsPlace.IN_HAND.getCards(game, isPlayer1);
        game.moveCard(Game.chooseRandomCard(inHandCards, false), inHandCards, game.getInGameCards());
    }
}
