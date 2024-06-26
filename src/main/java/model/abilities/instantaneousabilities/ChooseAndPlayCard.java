package model.abilities.instantaneousabilities;

import enums.cardsinformation.CardsPlace;
import model.Game;
import model.abilities.passiveabilities.DisruptMedic;
import model.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ChooseAndPlayCard extends InstantaneousAbility{
    private final Predicate<Card> function;
    private final CardsPlace cardsPlace;

    public ChooseAndPlayCard(CardsPlace cardsPlace, Predicate<Card> function) {
        this.function = function;
        this.cardsPlace = cardsPlace;
    }

    public void affect(Game game, Card myCard) {
        ArrayList<Card> cardsList = cardsPlace.getPlayerCards(game);
        List<Card> chooseCardsList = cardsList.stream().filter(function).toList();
        Card card = game.chooseCard(chooseCardsList, true, DisruptMedic.exists(game));
        game.moveCard(card, cardsList, game.getInGameCards());
    }
}
