package model.abilities.openingabilities;

import model.Game;
import model.abilities.Ability;

public abstract class OpeningAbility extends Ability {
    public abstract void affect(Game game, boolean isPlayer1);
    public static void StartRound(Game game) {
        Ability ability = game.getPlayer1LeaderCard().getABILITY();
        if (ability instanceof OpeningAbility) {
            ((OpeningAbility) ability).affect(game, true);
        }
        ability = game.getPlayer2LeaderCard().getABILITY();
        if (ability instanceof OpeningAbility) {
            ((OpeningAbility) ability).affect(game, false);
        }
    }
}
