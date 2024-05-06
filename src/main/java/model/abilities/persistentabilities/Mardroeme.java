package model.abilities.persistentabilities;

import model.abilities.Berserker;
import model.card.Card;

import java.util.ArrayList;

public class Mardroeme extends PersistentAbility {
    public static final ArrayList<Card> AffectedCards = new ArrayList<>();
    public Mardroeme(){
        super(CommandersHorn::doesAffectDefault);
    }

    public static boolean doesAffectDefault(Card abilityCard, Card card) {
        return sameRow(abilityCard, card) && !AffectedCards.contains(card) && card.getABILITY() instanceof Berserker;
    }
    @Override
    public ArrayList<Card> getAffectedCards() {
        return AffectedCards;
    }
    public static void affect(Card card, ArrayList<Card> inGameCards) {
        ((Berserker) card.getABILITY()).affect(inGameCards);
    }
}