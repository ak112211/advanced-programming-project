package model.ability;

import enums.Row;
import enums.cardsinformation.Type;
import model.*;
import model.card.Card;

public abstract class Ability {

	private Row row;
	private Game game;
	private Card card;

	public void setRow(Row row) {
		this.row = row;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Row getRow() {
		return row;
	}
	public static boolean canBeAffected(Card card) {
		return card.getTYPE().equals(Type.SPELL) && !card.getIS_HERO();
	}
	public static boolean sameRow(Card abilityCard, Card card) {
		return abilityCard.getRow().equals(card.getRow());
	}
	public static boolean notSameCards(Card abilityCard, Card card) {
		return abilityCard == card;
	}
	public static boolean sameName(Card abilityCard, Card card) {
		return abilityCard.getNAME().equals(card.getNAME());
	}

}