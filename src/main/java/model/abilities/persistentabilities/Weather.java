package model.abilities.persistentabilities;

import enums.Row;
import model.Game;
import model.abilities.passiveabilities.WeatherEndurance;
import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class Weather extends PersistentAbility {
    public static final ArrayList<Card> AFFECTED_CARDS = new ArrayList<>();

    public Weather(BiFunction<Card, Card, Boolean> doesAffect) {
        super(doesAffect);
    }

    public static boolean doesAffectCloseCombat(Card myCard, Card card) {
        return (card.getRow() == Row.PLAYER1_CLOSE_COMBAT || card.getRow() == Row.PLAYER2_CLOSE_COMBAT) && !AFFECTED_CARDS.contains(card);
    }

    public static boolean doesAffectSiege(Card myCard, Card card) {
        return (card.getRow() == Row.PLAYER1_SIEGE || card.getRow() == Row.PLAYER2_SIEGE) && !AFFECTED_CARDS.contains(card);
    }

    public static boolean doesAffectRanged(Card myCard, Card card) {
        return (card.getRow() == Row.PLAYER1_RANGED || card.getRow() == Row.PLAYER2_RANGED) && !AFFECTED_CARDS.contains(card);
    }

    public static boolean doesAffectRangedSiege(Card myCard, Card card) {
        return (doesAffectRanged(myCard, card) || doesAffectSiege(myCard, card));
    }

    @Override
    public ArrayList<Card> getAffectedCards() {
        return AFFECTED_CARDS;
    }

    public static void affect(Card card) {
        if (card.getFirstPower() != 0) {
            card.setPower(card.getPower() / card.getFirstPower()); // because of TightBond effect that made impact before weather
            if (WeatherEndurance.exists(Game.getCurrentGame())) {
                card.setPower(card.getPower() * (card.getFirstPower() / 2));
            }
        }
    }
}