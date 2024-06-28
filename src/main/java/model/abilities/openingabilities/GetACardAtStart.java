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
        if (isPlayer1) {
            game.player1GetRandomCard();
        } else {
            game.player2GetRandomCard();
        }
    }
}
