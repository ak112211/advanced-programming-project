package model.ability.persistentability;

import enums.cardsinformation.Type;
import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class CommandersHorn extends PersistentAbility {
    private static final ArrayList<Card> AffectedCards = new ArrayList<>();
    public CommandersHorn(BiFunction<Card, Card, Boolean> doesAffect){
        super(doesAffect);
    }
    public CommandersHorn(){
        super(CommandersHorn::doesAffectDefault);
    }

    public static boolean doesAffectSpy(Card abilityCard, Card card){
        return card.getTYPE().equals(Type.SPY_UNIT) && !AffectedCards.contains(card);
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
        card.setChangedPower(card.getChangedPower()*2);
    }

}