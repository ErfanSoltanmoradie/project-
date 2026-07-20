package model.building;

import model.finalPart.GlobalTower;

import java.time.Duration;

public enum BuildingType {

    WOOD_MINE(new Cost(50, 30, 20, 0, 0, 0, 0, Duration.ofSeconds(1)), 7, 7),
    IRON_MINE(new Cost(60, 40, 30, 0, 0, 0, 0, Duration.ofSeconds(1)), 7, 7),
    STONE_MINE(new Cost(50, 30, 20, 0, 0, 0, 0, Duration.ofSeconds(1)), 7, 7),
    DIRTY_WATER_MINE(new Cost(50, 30, 20, 0, 0, 0, 0, Duration.ofSeconds(1)), 7, 7),
    DIRTY_SOIL_MINE(new Cost(50, 30, 20, 0, 0, 0, 0, Duration.ofSeconds(1)), 7, 7),

    WATER_PURIFIER(new Cost(200, 100, 75, 0, 0, 0, 0, Duration.ofSeconds(1)), 8, 8),
    SOIL_PURIFIER(new Cost(200, 100, 75, 0, 0, 0, 0, Duration.ofSeconds(1)), 8, 8),

    GUNPOWDER_MINE(new Cost(70, 50, 40, 0, 0, 0, 0, Duration.ofSeconds(1)), 5, 5),
//REFINERY(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),

    WOOD_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),
    IRON_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),
    STONE_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),
    WATER_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),
    SOIL_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),
    GUNPOWDER_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),

    BALLISTA_DEFENSIVE(new Cost(200, 160, 100, 60, 40, 40, 0, Duration.ofSeconds(2)), 6, 6),
    CATAPULT_DEFENSIVE(new Cost(250, 240, 200, 120, 60, 60, 0, Duration.ofSeconds(2)), 6, 6),
    SENTINEL_DEFENSIVE(new Cost(300, 300, 250, 150, 80, 80, 0, Duration.ofSeconds(2)), 6, 6),
    BARRACKS(new Cost(250, 250, 150, 150, 0, 0, 0, Duration.ofSeconds(2)), 9, 9),

    LABORATORY(new Cost(350, 250, 200, 0, 70, 75, 0, Duration.ofSeconds(2)), 7, 7),
    RESEARCH_CENTER(new Cost(400, 300, 150, 0, 0, 0, 0, Duration.ofSeconds(2)), 7, 7),
    ARMY_PRODUCER(new Cost(300, 250, 150, 100, 0, 0, 0, Duration.ofSeconds(2)), 9, 9),
    MAJOR_BUILDING(new Cost(400, 300, 150,0, 150, 150, 0, Duration.ofSeconds(2)), 10, 10),
// PURIFICATION(new Cost(10, 10, 10, 10, 10, 10, 10, Duration.ofSeconds(2)), 1, 1),

    CUSTOMHOUSE(new Cost(3, 2, 500, 400, 300, 0, 100, 100, 0, Duration.ofSeconds(4)), 9, 9);
    BuildingType(Cost baseBuildCost, int width, int height) {
        this.baseBuildCost = baseBuildCost;
        this.width = width;
        this.height = height;
    }

    public Cost getBaseBuildCost() {
        return baseBuildCost;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight() {
        return height;
    }

    private final int width;
    private final int height;
    private final Cost baseBuildCost;
}
