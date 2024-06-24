package model.abilities.instantaneousabilities;

import enums.cardsinformation.CardsPlace;
import model.Game;
import model.card.Card;

import java.util.ArrayList;

public class GetCard {
    CardsPlace cardsPlace;
    boolean fromOwn;
    int amount;
    boolean random;

    public GetCard(CardsPlace cardsPlace, boolean fromOwn, int amount, boolean random) {
        this.cardsPlace = cardsPlace;
        this.fromOwn = fromOwn;
        this.amount = amount;
        this.random = random;
    }
}
