package model.building;

import java.time.Duration;

public enum BuildingType {

    WOOD_MINE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    IRON_MINE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    STONE_MINE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    DIRTY_WATER_MINE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    DIRTY_SOIL_MINE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),

    WATER_PURIFIER(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    SOIL_PURIFIER(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),

    GUNPOWDER_MINE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    REFINERY(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),

    WOOD_STORAGE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    IRON_STORAGE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    STONE_STORAGE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    WATER_STORAGE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    SOIL_STORAGE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),
    GUNPOWDER_STORAGE(new Cost(10, 10, 10, 10,10,10,10, Duration.ofSeconds(2))),

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
