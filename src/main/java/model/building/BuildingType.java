package model.building;

import java.time.Duration;

public enum BuildingType {

    WOOD_MINE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),
    IRON_MINE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),
    STONE_MINE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),
    DIRTY_WATER_MINE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),
    DIRTY_SOIL_MINE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),

    WATER_PURIFIER(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),
    SOIL_PURIFIER(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),

    GUNPOWDER_MINE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),
    REFINERY(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),

    WOOD_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),
    IRON_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),
    STONE_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),
    WATER_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),
    SOIL_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),
    GUNPOWDER_STORAGE(new Cost(10, 10, 10, 0, 0, 0, 0, Duration.ofSeconds(1))),

    BALLISTA_DEFENSIVE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    CATAPULT_DEFENSIVE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    SENTINEL_DEFENSIVE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    BARRACKS(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),

    LABORATORY(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    RESEARCH_CENTER(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    ARMY_PRODUCER(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    MAJOR_BUILDING(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    PURIFICATION(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    CUSTOMS(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2)));

    BuildingType(Cost baseBuildCost) {
        this.baseBuildCost = baseBuildCost;
    }

    public Cost getBaseBuildCost() {
        return baseBuildCost;
    }

    private final Cost baseBuildCost;
}
