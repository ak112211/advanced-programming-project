package model.abilities.persistentabilities;

import model.abilities.Berserker;
import model.card.Card;

import java.util.ArrayList;

public class Mardroeme extends PersistentAbility {
    public static final ArrayList<Card> affectedCards = new ArrayList<>();

    public Mardroeme() {
        super(Mardroeme::doesAffectDefault, "mardroeme");
    }

    public static boolean doesAffectDefault(Card myCard, Card card) {
        return card.getAbility() instanceof Berserker && myCard.sameRow(card) && !affectedCards.contains(card) ;
    }

    @Override
    public ArrayList<Card> getAffectedCards() {
        return affectedCards;
    }

    public static void affect(Card card, ArrayList<Card> inGameCards) {
        ((Berserker) card.getAbility()).affect(inGameCards, card);
    }
}