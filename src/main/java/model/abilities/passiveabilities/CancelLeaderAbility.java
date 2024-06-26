package model.abilities.passiveabilities;

import model.Game;
import model.abilities.Ability;

public class CancelLeaderAbility extends PassiveAbility {
    public static boolean exists(Game game) {
        return game.getPlayer1LeaderCard().getABILITY() instanceof CancelLeaderAbility ||
                game.getPlayer2LeaderCard().getABILITY() instanceof CancelLeaderAbility;
    }
}
