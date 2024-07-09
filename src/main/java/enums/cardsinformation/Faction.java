package enums.cardsinformation;

import enums.cards.*;
import javafx.scene.image.Image;
import model.card.Card;
import view.Tools;

import java.util.ArrayList;
import java.util.List;

public enum Faction {
    MONSTER(MonstersCards.values(), "monsters"),
    EMPIRE_NILFGAARDIAM(EmpireNilfgaardianCards.values(), "nilfgaard"),
    REALMS_NORTHERN(RealmsNorthernCards.values(), "realms"),
    SCOIA_TAEL(ScoiaTaelCards.values(), "scoiatael"),
    NEUTRAL(NeutralCards.values(), null),
    SKELLIGE(SkelligeCards.values(), "skellige"),;

    private final CardEnum[] cardEnumList;
    private final String iconName;

    Faction(CardEnum[] cardEnumList, String iconName) {
        this.cardEnumList = cardEnumList;
        this.iconName = iconName;
    }

    public Image getIcon() {
        return Tools.getImage("/gwentImages/img/icons/deck_shield_" + iconName + ".png");
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
