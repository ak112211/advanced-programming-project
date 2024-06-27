package model.abilities.instantaneousabilities;

import enums.cards.CardEnum;
import enums.cardsinformation.CardsPlace;
import enums.cardsinformation.Type;
import model.Game;
import model.card.Card;

import java.util.ArrayList;

public class PlayCard extends InstantaneousAbility {
    private final CardEnum CARD_ENUM;
    private final CardsPlace CARDS_PLACE;
    private final Type TYPE;

    public PlayCard(CardsPlace cardsPlace, CardEnum cardEnum, Type type) {
        CARD_ENUM = cardEnum;
        CARDS_PLACE = cardsPlace;
        TYPE = type;
    }

    public PlayCard(CardsPlace cardsPlace, CardEnum cardEnum) {
        CARD_ENUM = cardEnum;
        CARDS_PLACE = cardsPlace;
        TYPE = null;
    }

    public void affect(Game game, Card myCard) {
        Card card;
        if (CARDS_PLACE != null) {
            ArrayList<Card> cardsList = CARDS_PLACE.getPlayerCards(game);
            int index = cardsList.stream().map(Card::getCardEnum).toList().indexOf(CARD_ENUM);
            if (index == -1) {
                return;
            } else {
                card = cardsList.get(index);
                if (TYPE == null) {
                    card.setDefaultRow(game.isPlayer1Turn());
                } else {
                    card.setRow(TYPE.getRow(game.isPlayer1Turn()));
                }
                if (game.canPlay(card)) {
                    game.moveCard(card, cardsList, game.getInGameCards());
                }
            }
        } else {
            card = CARD_ENUM.getCard();
            if (TYPE == null) {
                card.setDefaultRow(game.isPlayer1Turn());
            } else {
                card.setRow(TYPE.getRow(game.isPlayer1Turn()));
            }
            if (game.canPlay(card)) {
                game.getInGameCards().add(card);
            }
        }
    }
}
