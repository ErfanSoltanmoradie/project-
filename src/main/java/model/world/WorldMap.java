package model.world;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class WorldMap {
    public static final int MAP_SIZE = 140;
    private Set<Coordinate> usedCoordinates = new HashSet<>();
    private final Random rand = new Random();


    public Coordinate getRandomCoordinate() {
        if (usedCoordinates.size() >= (MAP_SIZE / 14) * (MAP_SIZE / 14)) {
            return null;
        }
        Coordinate coordinate;
        do {
            int x = (rand.nextInt(0, MAP_SIZE / 14) * 14) + 2;
            int y = (rand.nextInt(0, MAP_SIZE / 14) * 14) + 2;
            coordinate = new Coordinate(x, y);
        } while (usedCoordinates.contains(coordinate));
        usedCoordinates.add(coordinate);
        return coordinate;
    }
    public void releaseCoordinate(Coordinate coordinate) {
        if (coordinate != null) {
            usedCoordinates.remove(coordinate);
        }
    }
}