package model.abilities.passiveabilities;

import model.Game;

public class CancelLeaderAbility extends PassiveAbility {
    public static boolean exists(Game game) {
        return game.getPlayer1LeaderCard().getAbility() instanceof CancelLeaderAbility ||
                game.getPlayer2LeaderCard().getAbility() instanceof CancelLeaderAbility;
    }
}
