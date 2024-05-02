package model;

import model.card.Card;

import java.util.ArrayList;

public class Deck {
    private String name;
    private String fileAddress;
    private ArrayList<Card> cards = new ArrayList<Card>();

    public ArrayList<Card> getCards() {
        return cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }
}
