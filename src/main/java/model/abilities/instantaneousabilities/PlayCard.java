package model.abilities.instantaneousabilities;

import enums.cards.CardEnum;
import enums.cardsinformation.CardsPlace;
import enums.cardsinformation.Type;
import model.Game;
import model.card.Card;

import java.util.ArrayList;

public class PlayCard extends InstantaneousAbility{
    private final CardEnum cardEnum;
    private final CardsPlace cardsPlace;
    private final Type type;
    public PlayCard(CardsPlace cardsPlace, CardEnum cardEnum, Type type) {
        this.cardEnum = cardEnum;
        this.cardsPlace = cardsPlace;
        this.type = type;
    }

    public PlayCard(CardsPlace cardsPlace, CardEnum cardEnum) {
        this.cardEnum = cardEnum;
        this.cardsPlace = cardsPlace;
        this.type = null;
    }

    public void affect(Game game, Card myCard) {
        Card card;
        if (cardsPlace != null) {
            ArrayList<Card> cardsList = cardsPlace.getCards(game, game.isPlayer1Turn());
            int index = cardsList.stream().map(Card::getCardEnum).toList().indexOf(cardEnum);
            if (index == -1) {
                return;
            } else {
                card = cardsList.get(index);
                if (type == null) {
                    card.setDefaultRow(game.isPlayer1Turn());
                } else {
                    card.setRow(type.getRow(game.isPlayer1Turn()));
                }
                if (game.canPlay(card)){
                    game.moveCard(card, cardsList, game.getInGameCards());
                }
            }
        }
        else {
            card = cardEnum.getCard();
            if (type == null) {
                card.setDefaultRow(game.isPlayer1Turn());
            } else {
                card.setRow(type.getRow(game.isPlayer1Turn()));
            }
            if (game.canPlay(card)) {
                game.getInGameCards().add(card);
            }
        }
    }
}
