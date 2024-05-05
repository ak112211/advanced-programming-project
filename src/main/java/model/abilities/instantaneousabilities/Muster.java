package model.abilities.instantaneousabilities;

import enums.Row;
import model.Game;
import model.card.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Muster extends InstantaneousAbility {
    private List<String> cardNames;
    public Muster(Card... cards){
        this.cardNames = Arrays.stream(cards).map(Card::getNAME).toList();
    }

    private boolean canMuster(Card card) {
        return !(sameName(getCard(),card) || cardNames.contains(card.getNAME()));
    }
    public void affect(Game game){
        if (getCard().getRow().isPlayer1()){
            for(int i = 0; i < game.getPlayer1Deck().size();){
                if (canMuster(game.getPlayer1Deck().get(i))){
                    game.player1PlayCardFromDeck(game.getPlayer1Deck().get(i), null);
                } else {
                    i++;
                }
            }
            for(int i = 0; i < game.getPlayer1InHandCards().size();){
                if (canMuster(game.getPlayer1InHandCards().get(i))){
                    game.player1PlayCard(game.getPlayer1InHandCards().get(i), null);
                } else {
                    i++;
                }
            }
        } else {
            for(int i = 0; i < game.getPlayer2Deck().size();){
                if (canMuster(game.getPlayer2Deck().get(i))){
                    game.player2PlayCardFromDeck(game.getPlayer2Deck().get(i), null);
                } else {
                    i++;
                }
            }
            for(int i = 0; i < game.getPlayer2InHandCards().size();){
                if (canMuster(game.getPlayer2InHandCards().get(i))){
                    game.player2PlayCard(game.getPlayer2InHandCards().get(i), null);
                } else {
                    i++;
                }
            }
        }
    }
}