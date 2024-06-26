package model.abilities.persistentabilities;

import enums.Row;
import enums.cardsinformation.Type;
import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class Weather extends PersistentAbility {
    public static ArrayList<Card> AffectedCards = new ArrayList<>();
//    private Type type;
//
//    public Weather(Type type) {
//        super(Weather::doesAffectDefault);
//        this.type = type;
//    }

//    public static boolean doesAffectDefault(Card myCard, Card card) {
//
//    }

    public Weather(BiFunction<Card, Card, Boolean> doesAffect) {
        super(doesAffect);
    }

    public static Weather newInstance(Type type) {
        if (type == Type.CLOSE_COMBAT_UNIT) {
            return new Weather(Weather::doesAffectCloseCombat);
        } else if (type == Type.RANGED_UNIT) {
            return new Weather(Weather::doesAffectRanged);
        } else if (type == Type.SIEGE_UNIT) {
            return new Weather(Weather::doesAffectSiege);
        } else if (type == Type.AGILE_UNIT) {
            return new Weather(Weather::doesAffectRangedSiege);
        }
        throw new RuntimeException("");
    }

    public static boolean doesAffectCloseCombat(Card myCard, Card card) {
        return (card.getRow() == Row.PLAYER1_CLOSE_COMBAT || card.getRow() == Row.PLAYER2_CLOSE_COMBAT) && !AffectedCards.contains(card);
    }

    public static boolean doesAffectSiege(Card myCard, Card card) {
        return (card.getRow() == Row.PLAYER1_SIEGE || card.getRow() == Row.PLAYER2_SIEGE) && !AffectedCards.contains(card);
    }

    public static boolean doesAffectRanged(Card myCard, Card card) {
        return (card.getRow() == Row.PLAYER1_RANGED || card.getRow() == Row.PLAYER2_RANGED) && !AffectedCards.contains(card);
    }

    public static boolean doesAffectRangedSiege(Card myCard, Card card) {
        return (doesAffectRanged(myCard, card) || doesAffectSiege(myCard, card)) && !AffectedCards.contains(card);
    }

    @Override
    public ArrayList<Card> getAffectedCards() {
        return AffectedCards;
    }

    public static void affect(Card card) {
        if (card.getFirstPower() != 0)
            card.setPower(card.getPower() / card.getFirstPower()); // because of TightBond effect
    }
}