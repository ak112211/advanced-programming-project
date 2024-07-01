package enums.cardsinformation;

import enums.cards.*;
import model.card.Card;

import java.util.ArrayList;
import java.util.List;

public enum Faction {
    MONSTER(MonstersCards.values()),
    EMPIRE_NILFGAARDIAM(EmpireNilfgaardianCards.values()),
    REALMS_NORTHERN(RealmsNorthernCards.values()),
    SCOIA_TAEL(ScoiaTaelCards.values()),
    NEUTRAL(NeutralCards.values()),
    SKELLIGE(SkelligeCards.values());

    private final CardEnum[] cardEnumList;

    Faction(CardEnum[] cardEnumList) {
        this.cardEnumList = cardEnumList;
    }

    public List<Card> getAllCards() {
        if (this == NEUTRAL) {
            throw new RuntimeException("Can't get cards of neutral faction");
        }
        List<Card> cards = new ArrayList<>();
        for (CardEnum cardEnum : cardEnumList) {
            for (int i = 0; i < cardEnum.getNoOfCardsInGame(); i++) {
                cards.add(cardEnum.getCard());
            }
        }
        for (CardEnum cardEnum : NEUTRAL.cardEnumList) {
            for (int i = 0; i < cardEnum.getNoOfCardsInGame(); i++) {
                cards.add(cardEnum.getCard());
            }
        }
        return cards;
    }
}
