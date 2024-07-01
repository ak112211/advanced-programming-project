package model.abilities.instantaneousabilities;

import enums.Row;
import model.Game;
import model.card.Card;

public class ClearWeather extends InstantaneousAbility {
    public ClearWeather() {
        setIconPath("clear");
    }

    public void affect(Game game, Card myCard) {
        for (int i = 0; i < game.getInGameCards().size(); i++) {
            if (game.getInGameCards().get(i).getRow().isWeather()) {
                game.moveCardToGraveyard(game.getInGameCards().get(i)); // It moves itself too.
            }
        }
    }
}