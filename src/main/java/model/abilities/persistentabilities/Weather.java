package model.abilities.persistentabilities;

import enums.Row;
import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class Weather extends PersistentAbility {
    public static ArrayList<Card> AffectedCards = new ArrayList<>();
    public Weather(BiFunction<Card, Card, Boolean> doesAffect){
        super(doesAffect);
    }
    public static boolean doesAffectCloseCombat(Card abilityCard, Card card){
        return (card.getRow() == Row.PLAYER1_CLOSE_COMBAT || card.getRow() == Row.PLAYER2_CLOSE_COMBAT) && !AffectedCards.contains(card);
    }
    public static boolean doesAffectSiege(Card abilityCard, Card card){
        return (card.getRow() == Row.PLAYER1_SIEGE || card.getRow() == Row.PLAYER2_SIEGE) && !AffectedCards.contains(card);
    }
    public static boolean doesAffectRanged(Card abilityCard, Card card){
        return (card.getRow() == Row.PLAYER1_RANGED || card.getRow() == Row.PLAYER2_RANGED) && !AffectedCards.contains(card);
    }
    public static boolean doesAffectRangedSiege(Card abilityCard, Card card){
        return (doesAffectRanged(abilityCard, card) || doesAffectSiege(abilityCard, card)) && !AffectedCards.contains(card);
    }
    @Override
    public ArrayList<Card> getAffectedCards() {
        return AffectedCards;
    }
    public static void affect(Card card) {
        if (card.getFIRST_POWER() != 0)
            card.setPower(card.getPower()/ card.getFIRST_POWER()); // because of TightBond effect
    }
}