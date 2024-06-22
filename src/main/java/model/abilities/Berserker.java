package model.abilities;

import model.card.Card;
import java.util.ArrayList;

public class Berserker extends Ability {
    private final Card newCard;
    public Berserker (Card newCard){
        this.newCard = newCard;
    }
    public void affect(ArrayList<Card> inGameCards){
        int i = inGameCards.indexOf(getCard());
        inGameCards.set(i, newCard);
    }

}