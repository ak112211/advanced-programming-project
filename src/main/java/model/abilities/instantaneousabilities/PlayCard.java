package model.abilities.instantaneousabilities;

import enums.cards.CardEnum;
import enums.cardsinformation.CardsPlace;
import model.Game;
import model.card.Card;

import java.util.ArrayList;

public class PlayCard extends InstantaneousAbility{
    private CardEnum cardEnum;
    private CardsPlace cardsPlace;
    PlayCard(CardEnum cardEnum, CardsPlace cardsPlace) {
        this.cardEnum = cardEnum;
        this.cardsPlace = cardsPlace;
    }

    public void affect(Game game, Card myCard) {
        if (cardsPlace != null) {
            ArrayList<Card> cardsList = cardsPlace.getCards(game, game.isPlayer1Turn());
            int index = cardsList.stream().map(Card::getCardEnum).toList().indexOf(cardEnum);
            if (index == -1) {
                return;
            } else {
                cardsList.remove(index);
            }
        }
        Card card = cardEnum.getCard();

    }
}
