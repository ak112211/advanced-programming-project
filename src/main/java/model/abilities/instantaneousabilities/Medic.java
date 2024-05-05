package model.abilities.instantaneousabilities;

import enums.Row;
import model.Game;
import model.card.Card;

public class Medic extends InstantaneousAbility {
    public void affect(Game game, Card card, Row row){
        game.resurrectCard(card, row);
    }
}