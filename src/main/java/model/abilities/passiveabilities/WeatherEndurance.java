package model.abilities.passiveabilities;

import model.Game;

public class WeatherEndurance extends PassiveAbility{
    public static boolean exists(Game game) {
        return (game.getPlayer1LeaderCard().getABILITY() instanceof WeatherEndurance ||
                game.getPlayer2LeaderCard().getABILITY() instanceof WeatherEndurance) &&
        !CancelLeaderAbility.exists(game);
    }
}
