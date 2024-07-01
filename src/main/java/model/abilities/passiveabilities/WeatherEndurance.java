package model.abilities.passiveabilities;

import model.Game;

public class WeatherEndurance extends PassiveAbility{
    public static boolean exists(Game game) {
        return (game.getPlayer1LeaderCard().getAbility() instanceof WeatherEndurance ||
                game.getPlayer2LeaderCard().getAbility() instanceof WeatherEndurance) &&
        !CancelLeaderAbility.exists(game);
    }
}
