package model.abilities.passiveabilities;

import model.Game;
import model.abilities.Ability;

public class PassiveAbility extends Ability {
    public static boolean exists(Game game, boolean player1) {
        return (player1 ? game.getPlayer1LeaderCard() : game.getPlayer2LeaderCard())
                .getABILITY() instanceof CancelLeaderAbility;
    }
}
