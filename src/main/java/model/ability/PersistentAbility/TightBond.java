package model.ability.PersistentAbility;

import model.ability.Ability;
import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class TightBond extends PersistentAbility {
    public static ArrayList<Card> AffectedCards = new ArrayList<>();
    public TightBond(BiFunction<Card, Card, Boolean> doesAffect){
        super(doesAffect);
    }
    public TightBond(){
        super(CommandersHorn::doesAffectDefault);
    }
    public static boolean doesAffectDefault(Card abilityCard, Card card) {
        return notSameCards(abilityCard, card) && sameName(abilityCard, card) && sameRow(abilityCard, card);
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
        card.setChangedPower(card.getChangedPower()+card.getPOWER());
    }

}