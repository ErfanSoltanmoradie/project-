package model.player;

import model.village.Village;
import model.world.Coordinate;
import model.world.WorldMap;

import java.util.UUID;

public class PlayerFactory {
    private WorldMap map;

    public PlayerFactory(WorldMap map) {
        this.map = map;
    }

    public Player createPlayer(String username) {
        Coordinate coordinate = map.getRandomCoordinate();
        if (coordinate == null) {
            throw new RuntimeException("Map is full, no new players allowed!");
        }
        Village village = new Village(coordinate, 200);
        Player player = new Player(username, village);
        return player;
    }


}
