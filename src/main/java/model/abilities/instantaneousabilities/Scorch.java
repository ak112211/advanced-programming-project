package model.abilities.instantaneousabilities;

import enums.Row;
import enums.cardsinformation.Type;
import model.Game;
import model.abilities.Ability;
import model.card.Card;

import java.util.ArrayList;
import java.util.List;

public class Scorch extends InstantaneousAbility {
    private final Type type;

    /**
     * @param type selects which row should it remove,
     *             type=null means it can affect every rows,
     *             type=AGILE_UNIT means it can affect only enemy
     */
    public Scorch(Type type) {
        this.type = type;
        setIconPath("scorch");
    }

    private List<Card> getCardsInRow(Game game, Card myCard) {
        boolean killPlayer1 = !game.isPlayer1Turn();
        ArrayList<Card> inGameCards = (ArrayList<Card>) game.getInGameCards().clone();
        if (type == null) { // affects every rows
            game.moveCardToGraveyard(myCard); // to remove scorch card
            return inGameCards;
        } else if (type == Type.AGILE_UNIT) { // affects enemy
            return inGameCards.stream().filter(card -> card.getRow().isPlayer1() == killPlayer1).toList();
        } else {
            Row row = type.getRow(killPlayer1);
            return inGameCards.stream().filter(card -> card.getRow() == row).toList();
        }
    }

    public void affect(Game game, Card myCard) {
        List<Card> cards = getCardsInRow(game, myCard).stream().filter(Ability::canBeAffected).toList();
        if (type == null || type == Type.AGILE_UNIT || cards.stream().mapToInt(Card::getPower).sum() > 10) {
            int maxPower = cards.stream().mapToInt(Card::getPower).max().orElse(0); // if it's empty maxPower value isn't important
            cards.stream().filter(card -> card.getPower() == maxPower)
                    .forEach(game::moveCardToGraveyard);
        }
    }
}