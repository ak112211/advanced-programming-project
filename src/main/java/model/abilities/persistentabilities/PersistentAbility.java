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
        CommandersHorn.AFFECTED_CARDS.clear();
        Mardroeme.AFFECTED_CARDS.clear();
        MoraleBoost.AFFECTED_CARDS.clear();
        TightBond.AFFECTED_CARDS.clear();
        Weather.AFFECTED_CARDS.clear();
        for (Card card : inGameCards) {
            if (card.getAbility() instanceof PersistentAbility) {
                ((PersistentAbility) card.getAbility()).addToAffectedCards(inGameCards, card);
            }
        }
    }

    public static void calculatePowers(ArrayList<Card> inGameCards) {
        findAffectedCards(inGameCards);
        for (Card card : Mardroeme.AFFECTED_CARDS) {
            Mardroeme.affect(card, inGameCards);
        }
        if (!Mardroeme.AFFECTED_CARDS.isEmpty()) {
            findAffectedCards(inGameCards);
        }
        for (Card card : inGameCards) {
            card.setPower(card.getFirstPower());
        }
        for (Card card : TightBond.AFFECTED_CARDS) {
            TightBond.affect(card);
        }
        for (Card card : Weather.AFFECTED_CARDS) {
            Weather.affect(card);
        }
        for (Card card : MoraleBoost.AFFECTED_CARDS) {
            MoraleBoost.affect(card);
        }
        for (Card card : CommandersHorn.AFFECTED_CARDS) {
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