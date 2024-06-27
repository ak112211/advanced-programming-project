package enums.cardsinformation;

import enums.cards.*;
import model.card.Card;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum Faction {
    MONSTER(MonstersCards.values()),
    EMPIRE_NILFGAARDIAM(EmpireNilfgaardianCards.values()),
    REALMS_NORTHERN(RealmsNorthernCards.values()),
    SCOIA_TAEL(ScoiaTaelCards.values()),
    NEUTRAL(NeutralCards.values()),
    SKELLIGE(SkelligeCards.values()),
    ;

    private final CardEnum[] CARDS_ENUM;

    Faction(CardEnum[] cardEnums) {
        CARDS_ENUM = cardEnums;
    }

    public List<Card> getAllCards() {
        if (this == NEUTRAL) {
            throw new RuntimeException("Can't get cards of neutral faction");
        }
        return Stream.concat(Arrays.stream(CARDS_ENUM), Arrays.stream(NEUTRAL.CARDS_ENUM)).map(CardEnum::getCard).toList();
    }
}
