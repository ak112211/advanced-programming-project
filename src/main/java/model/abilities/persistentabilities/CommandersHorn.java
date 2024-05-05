package model.abilities.persistentabilities;

import enums.cardsinformation.Type;
import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class CommandersHorn extends PersistentAbility {
    public static final ArrayList<Card> AffectedCards = new ArrayList<>();
    public CommandersHorn(BiFunction<Card, Card, Boolean> doesAffect){
        super(doesAffect);
    }
    public CommandersHorn(){
        super(CommandersHorn::doesAffectDefault);
    }

    public static boolean doesAffectDefault(Card abilityCard, Card card){
        return canBeAffected(card) && notSameCards(abilityCard, card) && sameRow(abilityCard, card) && !AffectedCards.contains(card);
    }
    public static boolean doesAffectSpy(Card abilityCard, Card card){
        return card.getTYPE() == Type.SPY_UNIT && !AffectedCards.contains(card);
    }
    @Override
    public ArrayList<Card> getAffectedCards() {
        return AffectedCards;
    }
    public static void affect(Card card) {
        card.setChangedPower(card.getChangedPower()*2);
    }

}