package model.abilities.persistentabilities;

import model.abilities.Ability;
import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public abstract class PersistentAbility extends Ability {
    private final BiFunction<Card, Card, Boolean> doesAffect;

    public PersistentAbility(BiFunction<Card, Card, Boolean> doesAffect, String iconName) {
        if (doesAffect == null) {
            throw new NullPointerException("doesAffect is null");
        }
        setIconPath(iconName);
        this.doesAffect = doesAffect;
    }

    public static void findAffectedCards(ArrayList<Card> inGameCards) {
        CommandersHorn.affectedCards.clear();
        Mardroeme.affectedCards.clear();
        MoraleBoost.affectedCards.clear();
        TightBond.affectedCards.clear();
        Weather.affectedCards.clear();
        for (Card card : inGameCards) {
            if (card.getAbility() instanceof PersistentAbility) {
                ((PersistentAbility) card.getAbility()).addToAffectedCards(inGameCards, card);
            }
        }
    }

    public static void calculatePowers(ArrayList<Card> inGameCards) {
        findAffectedCards(inGameCards);
        for (Card card : Mardroeme.affectedCards) {
            Mardroeme.affect(card, inGameCards);
        }
        if (!Mardroeme.affectedCards.isEmpty()) {
            findAffectedCards(inGameCards);
        }
        for (Card card : inGameCards) {
            card.setPower(card.getFirstPower());
        }
        for (Card card : TightBond.affectedCards) {
            TightBond.affect(card);
        }
        for (Card card : Weather.affectedCards) {
            Weather.affect(card);
        }
        for (Card card : MoraleBoost.affectedCards) {
            MoraleBoost.affect(card);
        }
        for (Card card : CommandersHorn.affectedCards) {
            CommandersHorn.affect(card);
        }
    }

    public abstract ArrayList<Card> getAffectedCards();

    public void addToAffectedCardsForEachCard(Card card, Card myCard) {
        if (doesAffect.apply(myCard, card)) {
            getAffectedCards().add(card);
        }
    }

    public void addToAffectedCards(ArrayList<Card> inGameCards, Card myCard) {
        for (Card card : inGameCards.stream().filter(Ability::canBeAffected).toList()) {
            addToAffectedCardsForEachCard(card, myCard);
        }
    }

}