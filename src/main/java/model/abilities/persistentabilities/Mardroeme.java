package model.abilities.persistentabilities;

import model.abilities.Berserker;
import model.card.Card;

import java.util.ArrayList;

public class Mardroeme extends PersistentAbility {
    public static final ArrayList<Card> AFFECTED_CARDS = new ArrayList<>();

    public Mardroeme() {
        super(Mardroeme::doesAffectDefault);
    }

    public static boolean doesAffectDefault(Card myCard, Card card) {
        return sameRow(myCard, card) && !AFFECTED_CARDS.contains(card) && card.getAbility() instanceof Berserker;
    }

    @Override
    public ArrayList<Card> getAffectedCards() {
        return AFFECTED_CARDS;
    }

    public static void affect(Card card, ArrayList<Card> inGameCards) {
        ((Berserker) card.getAbility()).affect(inGameCards, card);
    }
}