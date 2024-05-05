package model.abilities.persistentabilities;

import model.abilities.Ability;
import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public abstract class PersistentAbility extends Ability {
    public BiFunction<Card, Card, Boolean> doesAffect;
    public PersistentAbility(BiFunction<Card, Card, Boolean> doesAffect){
        this.doesAffect = doesAffect;
    }
    public static void findAffectedCards(ArrayList<Card> inGameCards) {
        CommandersHorn.AffectedCards.clear();
        Mardroeme.AffectedCards.clear();
        MoraleBoost.AffectedCards.clear();
        TightBond.AffectedCards.clear();
        Weather.AffectedCards.clear();
        for (Card card : inGameCards) {
            if (card.getABILITY() instanceof PersistentAbility) {
                ((PersistentAbility) card.getABILITY()).addToAffectedCards(inGameCards);
            }
        }
    }
    public static void calculateChangedPower(ArrayList<Card> inGameCards){
        findAffectedCards(inGameCards);
        for (Card card : Mardroeme.AffectedCards){
            Mardroeme.affect(card, inGameCards);
        }
        if (!Mardroeme.AffectedCards.isEmpty()){
            findAffectedCards(inGameCards);
        }
        for (Card card : inGameCards){
            card.setPower(card.getFIRST_POWER());
        }
        for (Card card : TightBond.AffectedCards){
            TightBond.affect(card);
        }
        for (Card card : Weather.AffectedCards){
            Weather.affect(card);
        }
        for (Card card : MoraleBoost.AffectedCards){
            MoraleBoost.affect(card);
        }
        for (Card card : CommandersHorn.AffectedCards){
            CommandersHorn.affect(card);
        }
    }
    public abstract ArrayList<Card> getAffectedCards();
    public void addToAffectedCardsForEachCard(Card card){
        if (doesAffect.apply(this.getCard(),card)){
            getAffectedCards().add(card);
        }
    }
    public void addToAffectedCards(ArrayList<Card> inGameCards){
        for (Card card : inGameCards){
            addToAffectedCardsForEachCard(card);
        }
    }

}