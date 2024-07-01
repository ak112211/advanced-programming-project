package model.abilities.passiveabilities;

import model.Game;

public class DisruptMedic extends PassiveAbility{
    public static boolean exists(Game game) {
        return (game.getPlayer1LeaderCard().getAbility() instanceof DisruptMedic ||
                game.getPlayer2LeaderCard().getAbility() instanceof DisruptMedic) &&
                !CancelLeaderAbility.exists(game);
    }
}
