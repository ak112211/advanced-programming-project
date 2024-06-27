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
    SKELLIGE(SkelligeCards.values()),;

    final CardEnum[] cardEnums;

    Faction (CardEnum[] cardEnums) {
        this.cardEnums = cardEnums;
    }

    public List<Card> getAllCards() {
        if (this == NEUTRAL) {
            throw new RuntimeException("Can't get cards of neutral faction");
        }
        return Stream.concat(Arrays.stream(cardEnums),Arrays.stream(NEUTRAL.cardEnums)).map(CardEnum::getCard).toList();
    }
}
