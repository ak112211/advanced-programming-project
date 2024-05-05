package model.abilities;

import enums.Row;
import model.Game;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.InstantaneousAbility;
import model.card.Card;

import java.util.ArrayList;

public class Berserker extends Ability {
    public Berserker(){
         super();
    }

    public static class Scorch extends InstantaneousAbility {
        public static ArrayList<Card> AffectedCards = new ArrayList<>();
        /**
         * @param row
         * @param game
         * @param card
         */
        public Scorch(Row row, Game game, Card card) {
            super(row, game, card);
        }
    }
}