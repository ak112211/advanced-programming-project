package model.ability.PersistentAbility;

import enums.Row;
import model.ability.Ability;
import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class Weather extends PersistentAbility {
    public static ArrayList<Card> AffectedCards = new ArrayList<>();
    public Weather(BiFunction<Card, Card, Boolean> doesAffect){
        super(doesAffect);
    }
    public static boolean doesAffectCloseCombat(Card abilityCard, Card card){
        return (card.getRow().equals(Row.PLAYER1_CLOSE_COMBAT) || card.getRow().equals(Row.PLAYER2_CLOSE_COMBAT)) && !AffectedCards.contains(card);
    }
    public static boolean doesAffectSiege(Card abilityCard, Card card){
        return (card.getRow().equals(Row.PLAYER1_SIEGE) || card.getRow().equals(Row.PLAYER2_SIEGE)) && !AffectedCards.contains(card);
    }
    public static boolean doesAffectRanged(Card abilityCard, Card card){
        return (card.getRow().equals(Row.PLAYER1_RANGED) || card.getRow().equals(Row.PLAYER2_RANGED)) && !AffectedCards.contains(card);
    }
    public static boolean doesAffectRangedSiege(Card abilityCard, Card card){
        return (doesAffectRanged(abilityCard, card) || doesAffectSiege(abilityCard, card)) && !AffectedCards.contains(card);
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
        if (card.getPOWER() != 0)
            card.setChangedPower(1);
    }
}