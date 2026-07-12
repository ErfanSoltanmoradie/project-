package model.building;

import java.time.Duration;

public enum BuildingType {

    WOOD_MINE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),5, 5),
    IRON_MINE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),5, 5),
    STONE_MINE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),5, 5),
    DIRTY_WATER_MINE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),5, 5),
    DIRTY_SOIL_MINE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),5, 5),

    WATER_PURIFIER(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),
    SOIL_PURIFIER(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),

    GUNPOWDER_MINE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),5, 5),
    REFINERY(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),

    WOOD_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),
    IRON_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),
    STONE_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),
    WATER_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),
    SOIL_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),
    GUNPOWDER_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1)),1, 1),

    BALLISTA_DEFENSIVE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2)),1, 1),
    CATAPULT_DEFENSIVE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2)),1, 1),
    SENTINEL_DEFENSIVE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2)),1, 1),
    BARRACKS(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2)),1, 1),

    LABORATORY(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2)),1, 1),
    RESEARCH_CENTER(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2)),3, 3),
    ARMY_PRODUCER(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2)),1, 1),
    MAJOR_BUILDING(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2)),1, 1),
    PURIFICATION(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2)),1, 1),

    CUSTOMHOUSE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2)), 1, 1);

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
