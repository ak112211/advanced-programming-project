package model.abilities;

import enums.Row;
import model.App;
import model.Game;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.InstantaneousAbility;
import model.card.Card;

import java.util.ArrayList;

public class Berserker extends Ability {
    private Card newCard;
    public Berserker (){

    }
    public void affect(ArrayList<Card> inGameCards){
        int i = inGameCards.indexOf(getCard());
        inGameCards.set(i, newCard);
    }

}