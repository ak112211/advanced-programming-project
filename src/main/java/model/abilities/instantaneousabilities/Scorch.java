package model.abilities.instantaneousabilities;

import enums.Row;
import enums.cardsinformation.Type;
import model.Game;
import model.abilities.Ability;
import model.card.Card;

import java.util.ArrayList;
import java.util.List;

public class Scorch extends InstantaneousAbility {
    Type type;
    /**
    @param type selects which row should it remove, type=null means it can affect every rows
     */
    public Scorch(Type type) {
        this.type = type;
    }
    private List<Card> getCardsInRow(ArrayList<Card> inGameCards) {
        if (type == null){
            return inGameCards;
        }
        Row row = type.getRow(getCard().getRow().isPlayer1());
        return inGameCards.stream().filter(card -> card.getRow()==row).toList();
    }
    public void affect(Game game) {
        List<Card> cards = getCardsInRow(game.getInGameCards()).stream()
                .filter(Ability::canBeAffected).toList();
        if (type != null && cards.stream().mapToInt(Card::getPower).sum() > 10){
            int maxPower = cards.stream().mapToInt(Card::getPower).max().orElse(0); // if it's empty maxPower value isn't important
            cards.stream().filter(card -> card.getPower() == maxPower)
                    .forEach(game::moveCardToGraveyard);
        }
    }
}