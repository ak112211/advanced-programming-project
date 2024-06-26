package enums.leaders;

import enums.cards.NeutralCards;
import enums.cardsinformation.CardsPlace;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.GetCard;
import model.abilities.instantaneousabilities.PlayCard;
import model.abilities.instantaneousabilities.SeeCards;
import model.abilities.passiveabilities.CancelLeaderAbility;
import model.card.Leader;

public enum EmpireNilfgaardianLeaders {
    THE_WHITE_FLAME("The White Flame", new PlayCard(CardsPlace.DECK, NeutralCards.TORRENTIAL_RAIN), "Select and play a Torrential Rain card from your deck.", "/gwentImages/img/lg/nilfgaard_emhyr_silver.jpg"),
    HIS_IMPERIAL_MAJESTY("His Imperial Majesty", new SeeCards(3), "View three random cards from the opponent's hand.", "/gwentImages/img/lg/nilfgaard_emhyr_copper.jpg"),
    EMPEROR_OF_NILFGAARD("Emperor of Nilfgaard", new CancelLeaderAbility(), "Cancels the opponent's leader ability.", "/gwentImages/img/lg/nilfgaard_emhyr_bronze.jpg"),
    THE_RELENTLESS("The Relentless", new GetCard(CardsPlace.GRAVEYARD, false, 1, false), "Take a card from the opponent's graveyard (cannot take a Hero card).", "/gwentImages/img/lg/nilfgaard_emhyr_gold.jpg"),
    INVADER_OF_THE_NORTH("Invader of the North", null, "Revives a random card from both players' graveyards and returns them to play.", "/gwentImages/img/lg/nilfgaard_emhyr_invader_of_the_north.jpg");

    private final String name;
    private final Ability ability;
    private final String description;
    private final String imagePath;

    EmpireNilfgaardianLeaders(String name, Ability ability, String description, String imagePath) {
        this.name = name;
        this.ability = ability;
        this.description = description;
        this.imagePath = imagePath;
    }

    public Leader getLeader() {
        return new Leader(name, ability, Faction.EMPIRE_NILFGAARDIAM, description, imagePath);
    }
}
