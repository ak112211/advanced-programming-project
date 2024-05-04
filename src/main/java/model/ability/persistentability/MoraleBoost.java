package model.ability.persistentability;

import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class MoraleBoost extends PersistentAbility {
    public static ArrayList<Card> AffectedCards = new ArrayList<>();
    public MoraleBoost(BiFunction<Card, Card, Boolean> doesAffect){
        super(doesAffect);
    }
    public MoraleBoost(){
        super(CommandersHorn::doesAffectDefault);
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
        card.setChangedPower(card.getChangedPower()+1);
    }


}