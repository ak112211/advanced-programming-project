package model.ability.persistentability;

import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class Mardroeme extends PersistentAbility {
    private static final ArrayList<Card> AffectedCards = new ArrayList<>();
    public Mardroeme(BiFunction<Card, Card, Boolean> doesAffect){
        super(doesAffect);
    }
    public Mardroeme(){
        super(CommandersHorn::doesAffectDefault);
    }
    public static boolean doesAffectDefault(Card abilityCard, Card card) {
        return sameRow(abilityCard, card) && !AffectedCards.contains(card) && card.getABILITY().equals(enums.cardsinformation.Ability.BERSERKER); // instanceof Berserker; TODO
    }
    @Override
    public ArrayList<Card> getAffectedCards() {
        return AffectedCards;
    }
    @Override
    public void addToAffectedCards(Card card) {
        AffectedCards.add(card);
    }
    @Override
    public void affect(Card card) {
        //TODO
    }
}