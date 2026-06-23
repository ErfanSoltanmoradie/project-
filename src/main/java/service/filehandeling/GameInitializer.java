package service.filehandeling;

import model.player.Player;
import model.village.Village;

public class GameInitializer {

    public static void init(GameState state) {

        for (Player player : state.getPlayer().values()) {

            Village v = player.getVillage();

            v.runTimeServices();
        }
    }
}