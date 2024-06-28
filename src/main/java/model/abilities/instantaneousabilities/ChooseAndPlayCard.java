package model.abilities.instantaneousabilities;

import enums.cardsinformation.CardsPlace;
import model.Game;
import model.abilities.passiveabilities.DisruptMedic;
import model.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ChooseAndPlayCard extends InstantaneousAbility {
    private final Predicate<Card> FUNCTION;
    private final CardsPlace CARDS_PLACE;

    public ChooseAndPlayCard(CardsPlace cardsPlace, Predicate<Card> function) {
        FUNCTION = function;
        CARDS_PLACE = cardsPlace;
    }

    public void affect(Game game, Card myCard) {
        ArrayList<Card> cardsList = CARDS_PLACE.getPlayerCards(game);
        List<Card> chooseCardsList = cardsList.stream().filter(FUNCTION).toList();
        Optional<Card> card = game.chooseCard(chooseCardsList, true, DisruptMedic.exists(game));
        if (card.isEmpty()) {
            return;
        }
        game.moveCard(card.get(), cardsList, game.getInGameCards());
    }
}
